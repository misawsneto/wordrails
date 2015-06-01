package com.wordrails.business;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.util.WordpressParsedContent;
import com.wordrails.util.WordrailsUtil;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tika.Tika;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.exception.ConstraintViolationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordpressService {
    
    private static final Logger log = LoggerFactory.getLogger(ResponseErrorHandler.class);

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private TaxonomyRepository taxonomyRepository;
    @Autowired
    private FileContentsRepository contentsRepository;

    public WordpressPost createPost(Post post, WordpressApi api) throws Exception {
        WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());

        switch (post.state) {
            case (Post.STATE_SCHEDULED):
                wp.status = "future";
                wp.date = post.date;
                break;
            case (Post.STATE_PUBLISHED):
                wp.status = "publish";
                break;
            case (Post.STATE_DRAFT):
                wp.status = "draft";
                break;
        }

        return api.createPost(wp);
    }

    public Integer deletePost(Integer wordpressId, WordpressApi api) throws Exception {
        return api.deletePost(wordpressId).getStatus();
    }

    public WordpressPost updatePost(Post post, WordpressApi api) throws Exception {
        WordpressPost wp = new WordpressPost(post.title, post.body, post.state, new Date());

        switch (post.state) {
            case (Post.STATE_SCHEDULED):
                wp.status = "future";
                wp.date = post.date;
                break;
            case (Post.STATE_PUBLISHED):
                wp.status = "publish";
                break;
            case (Post.STATE_DRAFT):
                wp.status = "draft";
                break;
        }

        return api.editPost(post.wordpressId, wp);
    }

    public Post getPost(Post post, WordpressPost wpPost, HashBasedTable<String, Integer, Term> dbTerms, Taxonomy tagTaxonomy, Taxonomy categoryTaxonomy)
        throws ConstraintViolationException, DataIntegrityViolationException, Exception {
        post.body = wpPost.body;
        switch (wpPost.status) {
            case "publish":
                post.state = Post.STATE_PUBLISHED;
                break;
            case "draft":
                post.state = Post.STATE_DRAFT;
                break;
            case "future":
                post.state = Post.STATE_SCHEDULED;
                break;
        }
        post.date = wpPost.date;
        post.slug = wpPost.slug;
        post.originalSlug = wpPost.slug;
        post.title = wpPost.title;
        post.wordpressId = wpPost.id;
        post.lastModificationDate = wpPost.modified;

        if (wpPost.terms != null && (wpPost.terms.tags != null || wpPost.terms.categories != null)) {
            Map<Integer, WordpressTerm> terms = new HashMap<>();
            if (wpPost.terms.tags != null) {
                for (WordpressTerm tag : wpPost.terms.tags) {
                    terms.put(tag.id, tag);
                }
            }
            if (wpPost.terms.categories != null) {
                for (WordpressTerm category : wpPost.terms.categories) {
                    terms.put(category.id, category);
                }
            }

            post.terms = getTerms(terms, dbTerms, tagTaxonomy, categoryTaxonomy);
        }

        return post;
    }

    private Term selectTerm(HashBasedTable<String, Integer, Term> terms, Integer wordpressId, String slug, boolean isTag, Integer taxTagId) throws SecurityException {
        Term term = null;

        if (terms.containsColumn(wordpressId)) { //does it exist a term with this wordpressId?
            if (terms.contains(slug, wordpressId)) { //does this term have the same slug?
                term = terms.get(slug, wordpressId);
            } else if (terms.contains("", wordpressId)) { //is the slug of this term null?
                term = terms.get("", wordpressId);
                term.wordpressSlug = slug;

                termRepository.save(term);
            } else {//another wordpressId is using this slug. Data is fucked up somewhere. should never happen
                throw new SecurityException("Data stored in database is different from the terms sent in the request. Wordpress ID: "
                    + wordpressId + ", Slug: " + slug + ", is tag: " + isTag);
            }
        } else if (terms.containsRow(slug)) { //if this wp id does not exist, what about a term with the same slug?
            if (terms.contains(slug, 0)) { //is the wordpressId of this term null?
                term = terms.get(slug, 0);
                term.wordpressId = wordpressId;

                term = termRepository.save(term);
            } else {
                //in this case, there is another term using this slug but the wordpressId is neither null or the same. so the data is inconsistent if 
                //they belong to the same taxonomy. if they do, throw a exception

                Integer taxId = Lists.newArrayList(terms.rowMap().get(slug).values()).get(0).taxonomy.id;
                if ((isTag && Objects.equals(taxId, taxTagId)) || (!isTag && !Objects.equals(taxId, taxTagId))) { //if they are part of same taxonomy, it should not have the same slug. otherwise no prob
                    //another wordpressId is using this slug. Data is fucked up somewhere. should never happen
                    throw new SecurityException("Data stored in database is different from the terms sent in the request. Wordpress ID: "
                        + wordpressId + ", Slug: " + slug + ", is tag: " + isTag);
                }
            }
        } else {
            return null;
        }

        return term;
    }

    private Term saveNewTermWithParents(WordpressTerm term, Taxonomy tax, Map<Integer, WordpressTerm> wpTerms, HashBasedTable<String, Integer, Term> dbTerms) {
        Term t = new Term();
        t.wordpressId = term.id;
        t.name = term.name;
        t.taxonomy = tax;
        t.wordpressSlug = term.slug;

        if (term.parent != null && term.parent > 0) {
            Term termParent = null;
            if (dbTerms.containsColumn(term.parent)) {
                termParent = Lists.newArrayList(dbTerms.columnMap().get(term.parent).values()).get(0);
            } else if (wpTerms.containsKey(term.parent)) {
                termParent = saveNewTermWithParents(wpTerms.get(term.parent), tax, wpTerms, dbTerms); //it's recursive bitch
                termRepository.save(termParent);
            }
            t.parent = termParent;
        }

        t = termRepository.save(t);

        return t;
    }

    public Set<Term> getTerms(Map<Integer, WordpressTerm> wpTerms, HashBasedTable<String, Integer, Term> dbTerms, Taxonomy taxTag, Taxonomy taxCat)
        throws ConstraintViolationException, DataIntegrityViolationException, Exception {
        Set<Term> terms = new HashSet();
        for (WordpressTerm term : wpTerms.values()) {
            Term t = selectTerm(dbTerms, term.id, term.slug, term.isTag(), taxTag.id);
            if (t == null) { //does not exist. create new
                t = new Term();
                if (term.parent != null && term.parent > 0) {
                    if (dbTerms.containsColumn(term.parent)) { //if parent is in db, get from db map
                        t.parent = Lists.newArrayList(dbTerms.columnMap().get(term.parent).values()).get(0);
                    } else if (wpTerms.containsKey(term.parent)) { //if not in db, makes new and save it to db
                        t.parent = saveNewTermWithParents(wpTerms.get(term.parent), taxCat, wpTerms, dbTerms);
                    }
                }
                t.wordpressId = term.id;
                t.name = term.name;
                if (term.isTag()) {
                    t.taxonomy = taxTag;
                } else {
                    t.taxonomy = taxCat;
                }
                t.wordpressSlug = term.slug;

                try {
                    termRepository.save(t);
                    dbTerms.put(t.wordpressSlug, t.wordpressId, t);
                } catch (ConstraintViolationException | DataIntegrityViolationException e) {
                    t = termRepository.findByWordpressSlug(term.slug);
                    if (t != null) {
                        t.wordpressId = term.id;
                        try {
                            t = termRepository.save(t);
                        } catch (ConstraintViolationException | DataIntegrityViolationException e2) {
                            throw new Exception("Term ID=" + term.id + " " + ExceptionUtils.getStackTrace(e), e2);
                        }
                    }
                }
            }

            terms.add(t);
        }

        return terms;
    }

    public HashBasedTable<String, Integer, Term> getTermsByTaxonomy(Taxonomy taxonomy) {
        HashBasedTable<String, Integer, Term> dbTerms = HashBasedTable.create();
        List<Term> ts = termRepository.findByTaxonomy(taxonomy);
        for (Term term : ts) {
            if (term.wordpressSlug == null) {
                term.wordpressSlug = "";
            }
            if (term.wordpressId == null) {
                term.wordpressId = 0;
            }
            dbTerms.put(term.wordpressSlug, term.wordpressId, term);
        }

        return dbTerms;
    }

    @Async
    @Transactional
    public void sync(Wordpress wp, WordpressApi api) {
        Integer id = 0;
        while (true) {
            Set<WordpressPost> wpPosts = null;

            WordpressGetPostsParams params = new WordpressGetPostsParams();
            params.id = id;
            params.limit = 50;
            wpPosts = api.posts(params);

            if (wpPosts.isEmpty()) {
                break;
            }

            Post post = null;
            WordpressPost wpp = null;
            try {
                Taxonomy categoryTaxonomy = taxonomyRepository.findByWordpress(wp);
                Taxonomy tagTaxonomy = taxonomyRepository.findTypeTByWordpress(wp);
                HashBasedTable<String, Integer, Term> dbTerms = getTermsByTaxonomy(tagTaxonomy);
                dbTerms.putAll(getTermsByTaxonomy(categoryTaxonomy));

                Set<Post> posts = new HashSet<>();
                Set<String> slugs = postRepository.findSlugs();
                Set<Integer> wordpressIds = postRepository.findWordpressIdsByStation(tagTaxonomy.owningStation.id);
                for (WordpressPost wpPost : wpPosts) {
                    wpp = wpPost;
                    if (!wordpressIds.add(wpPost.id)) { //if wordpressId already exists in db
                        post = postRepository.findByWordpressId(wpPost.id);
                        post = getPost(post, wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                    } else {
                        Person author = personRepository.findByWordpressId(1); //temporary
                        Station station = stationRepository.findByWordpressId(wp.id);

                        post = getPost(new Post(), wpPost, dbTerms, tagTaxonomy, categoryTaxonomy);
                        post.station = station;
                        post.author = author;

                        if (!slugs.add(post.slug)) { //if slug already exists in db
                            String hash = WordrailsUtil.generateRandomString(5, "!Aau");
                            post.slug = post.slug + "-" + hash;
                        }
                    }

                    id = wpPost.id; //in the end the last id will prevail

                    posts.add(post);
                }

                processWordpressPost(posts);
                postRepository.save(posts);
            } catch (Exception e) {
                String msg = "";
                if (wpp != null) {
                    msg = "Post id=" + wpp.id + " ";
                }
                String error = e.getClass().getSimpleName() + ": " + msg + ExceptionUtils.getStackTrace(e);
                api.syncError(error);
                log.error(error);
            }

        }
    }

    @Transactional
    public void processWordpressPost(Post post) {
        WordpressParsedContent wcp = extractImageFromContent(post.body);
        post.body = wcp.content;
        post.featuredImage = wcp.image;
        post.externalFeaturedImgUrl = wcp.externalImageUrl;
    }

    @Transactional
    public void processWordpressPost(Collection<Post> posts) {
        for (Post post : posts) {
            WordpressParsedContent wcp = extractImageFromContent(post.body);
            post.body = wcp.content;
            post.featuredImage = wcp.image;
            post.externalFeaturedImgUrl = wcp.externalImageUrl;
        }
    }

    @Transactional
    public WordpressParsedContent extractImageFromContent(String content) {
        if (content == null || content.isEmpty()) {
            content = "";
        }
        Document doc = Jsoup.parse(content);
        // Get all img tags
        String featuredImageUrl = null;
        Elements imgs = doc.getElementsByTag("img");

        WordpressParsedContent wpc = new WordpressParsedContent();

        com.wordrails.business.File file = null;

        try {
            for (Element element : imgs) {
                String imageURL = element.attr("src");
                if (imageURL != null && !imageURL.isEmpty()) {
                    URL url;
                    try {
                        url = new URL(imageURL);
                        try (InputStream is = url.openStream()) {
                            try (ImageInputStream in = ImageIO.createImageInputStream(is)) {
                                final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                                if (readers.hasNext()) {
                                    ImageReader reader = readers.next();
                                    try {
                                        reader.setInput(in);
                                        int dimensions = reader.getWidth(0) * reader.getHeight(0);
                                        if (dimensions > 250000) {
                                            Element parent = element.parent();
                                            if (parent != null && parent.tagName().equals("a")) {
                                                parent.remove();
                                            }
                                            featuredImageUrl = imageURL;

                                            String fileName = FilenameUtils.getBaseName(featuredImageUrl);
                                            String fileFormat = FilenameUtils.getExtension(featuredImageUrl);
                                            String tempFileName = fileName + WordrailsUtil.generateRandomString(5, "Aa#") + "." + fileFormat;

                                            BufferedImage image = null;
                                            image = ImageIO.read(url);
                                            java.io.File dataFile = java.io.File.createTempFile(tempFileName, "." + fileFormat);
                                            ImageIO.write(image, fileFormat, dataFile);

                                            try (InputStream fileIS = new FileInputStream(dataFile)) {
                                                file = new File();
                                                file.type = File.INTERNAL_FILE;
                                                file.mime = file.mime == null || file.mime.isEmpty() ? new Tika().detect(fileIS) : file.mime;
                                                file.name = FilenameUtils.getBaseName(featuredImageUrl) + "." + FilenameUtils.getExtension(featuredImageUrl);
                                                fileRepository.save(file);
                                                Integer id = file.id;

                                                Session session = (Session) manager.getDelegate();
                                                LobCreator creator = Hibernate.getLobCreator(session);
                                                FileContents contents = contentsRepository.findOne(id);

                                                contents.contents = creator.createBlob(FileUtils.readFileToByteArray(dataFile));
                                                contentsRepository.save(contents);

                                                Image img = new Image();
                                                img.original = file;
                                                createImages(img);
                                                imageRepository.save(img);
                                                wpc.image = img;
                                            }

                                            break;
                                        }
                                    } finally {
                                        reader.dispose();
                                    }
                                }
                            } catch (IOException e) {
                            }
                        } catch (IOException e) {
                        }
                    } catch (MalformedURLException e1) {
                    }
                }
            }
        } catch (Exception e) {
        }

        wpc.content = doc.text();
        wpc.externalImageUrl = featuredImageUrl;
        wpc.content = wpc.content.replaceAll("\\[(.*?)\\](.*?)\\[/(.*?)\\]", "");
        wpc.content = wpc.content.trim();

        return wpc;
    }

    private void createImages(Image image) throws SQLException, IOException {
        com.wordrails.business.File original = image.original;

        String format = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;

        if (original != null) {
            com.wordrails.business.File small = new File();
            small.type = File.INTERNAL_FILE;
            small.mime = image.original.mime != null ? image.original.mime : MIME;
            small.name = original.name;
            fileRepository.save(small);

            com.wordrails.business.File medium = new File();
            medium.type = File.INTERNAL_FILE;
            medium.mime = image.original.mime != null ? image.original.mime : MIME;
            medium.name = original.name;
            fileRepository.save(medium);

            com.wordrails.business.File large = new File();
            large.type = File.INTERNAL_FILE;
            large.mime = image.original.mime != null ? image.original.mime : MIME;
            large.name = original.name;
            fileRepository.save(large);

            image.small = small;
            image.medium = medium;
            image.large = large;

            BufferedImage bufferedImage;
            FileContents contents = contentsRepository.findOne(original.id);
            try (InputStream input = contents.contents.getBinaryStream()) {
                bufferedImage = ImageIO.read(input);
            }
            image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
            updateContents(small.id, bufferedImage, 150, format);
            updateContents(medium.id, bufferedImage, 300, format);
            updateContents(large.id, bufferedImage, 1024, format);
        }
    }

    private static final String MIME = "image/jpeg";
    private static final String FORMAT = "jpg";
    private static final double QUALITY = 1;

    private void updateContents(Integer id, BufferedImage image, int size, String format) throws IOException {
        format = (format != null ? format : FORMAT);
        java.io.File file = java.io.File.createTempFile("image", "." + format);
        try {
            Thumbnails.of(image).size(size, size).outputFormat(format).outputQuality(QUALITY).toFile(file);
            Session session = (Session) manager.getDelegate();
            LobCreator creator = Hibernate.getLobCreator(session);
            FileContents contents = contentsRepository.findOne(id);
            contents.contents = creator.createBlob(FileUtils.readFileToByteArray(file));
            contentsRepository.save(contents);
        } finally {
            file.delete();
        }
    }
}
