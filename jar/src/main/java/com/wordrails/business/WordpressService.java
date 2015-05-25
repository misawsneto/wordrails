package com.wordrails.business;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.wordrails.persistence.TermRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class WordpressService {

    @Autowired
    private TermRepository termRepository;

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
        } else if (terms.containsRow(slug)) { //does it exist a term with this slug?
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
}
