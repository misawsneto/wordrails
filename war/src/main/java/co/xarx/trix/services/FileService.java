package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.dynalink.beans.StaticClass;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by misael on 4/12/2016.
 */
@Service
public class FileService {
	public static final Integer MAX_SIZE_8 = 8388608;
	public static final Integer MAX_SIZE_16 = 16777216;
	public static final Integer MAX_SIZE_32 = 33554432;
	public static final Integer MAX_SIZE_48 = 50331648;

	@Autowired
	public FileService(AmazonCloudService amazonCloudService, FileRepository fileRepository, DocumentInternalRepository
			documentInternalRepository, AudioInternalRepository audioInternalRepository, VideoInternalRepository videoInternalRepository) {
		this.amazonCloudService = amazonCloudService;
		this.fileRepository = fileRepository;
		this.documentInternalRepository = documentInternalRepository;
		this.audioInternalRepository = audioInternalRepository;
		this.videoInternalRepository = videoInternalRepository;
	}

	private AmazonCloudService amazonCloudService;
	private FileRepository fileRepository;
	private DocumentInternalRepository documentInternalRepository;
	private AudioInternalRepository audioInternalRepository;
	private VideoInternalRepository videoInternalRepository;

	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@Transactional
	public Video createAndSaveNewVideo(String name, java.io.File originalFile, String mime) throws
			Exception {

		String hash = null;
		try (InputStream is = new FileInputStream(originalFile)){
			hash = FileUtil.getHash(is);
		}

		VideoFile videoFile = new VideoFile(originalFile, hash);

		mime = mime != null && mime.contains("octet-stream") ? FileUtil.getMimeTypeFromName(name) : mime;

		Video existingImage = videoInternalRepository.findOne(QVideoInternal.videoInternal.file.hash.eq(videoFile.hash));
		if (existingImage != null) {
			return existingImage;
		}

		VideoInternal originalVideo = getOriginalVideo(mime, originalFile, videoFile, name);

		saveFile(name, originalFile, originalVideo.file);

		videoInternalRepository.save(originalVideo);

		return originalVideo;
	}

	public void saveFile(String name, java.io.File originalFile, File file){
		try{
			if(file.id == null) {
				FileMeta meta = null;
				try (FileInputStream is = new FileInputStream(originalFile)){
					meta = extractMetadata(name, is);
				}
				if(meta!=null)
					file.meta = simpleMapper.writeValueAsString(meta.metadata);
				fileRepository.save(file);
			}
		}catch (Throwable e){
			Logger.error("error getting metada data", e);
		}

	}

	@Transactional
	public AudioInternal createAndSaveNewAudio(String name, java.io.File originalFile, String mime) throws
			Exception {

		String hash = null;
		try (InputStream is = new FileInputStream(originalFile)){
			hash = FileUtil.getHash(is);
		}

		AudioFile audioFile = new AudioFile(originalFile, hash);

		mime = mime != null && mime.contains("octet-stream") ? FileUtil.getMimeTypeFromName(name) : mime;

		AudioInternal existingImage = audioInternalRepository.findOne(QAudioInternal.audioInternal.file.hash.eq(audioFile
				.hash));
		if (existingImage != null) {
			return existingImage;
		}

		AudioInternal originalAudio = getOriginalAudio(mime, originalFile, audioFile, name);

		saveFile(name, originalFile, originalAudio.file);

		audioInternalRepository.save(originalAudio);

		return originalAudio;
	}

	@Transactional
	public Document createAndSaveNewDoc(String name, java.io.File originalFile, String mime) throws
			Exception {

		String hash = null;
		try (InputStream is = new FileInputStream(originalFile)){
			hash = FileUtil.getHash(is);
		}

		DocumentFile docFile = new DocumentFile(originalFile, hash);

		mime = mime != null && mime.contains("octet-stream") ? FileUtil.getMimeTypeFromName(name) : mime;

		Document existingDocument = documentInternalRepository.findOne(QDocumentInternal.documentInternal.file.hash.eq(docFile.hash));
		if (existingDocument != null) {
			return existingDocument;
		}

		DocumentInternal originalDoc = getOriginalDocument(mime, originalFile, docFile, name);

		saveFile(name, originalFile, originalDoc.file);

		documentInternalRepository.save(originalDoc);

		return originalDoc;
	}

	public File createNewVideoTrixFile(String mime, Long size) {
		File file = new File();
		file.directory = File.DIR_VIDEO;
		file.type = File.EXTERNAL;
		file.mime = mime;
		file.size = size;

		return file;
	}

	public File createNewAudioTrixFile(String mime, Long size) {
		File file = new File();
		file.directory = File.DIR_AUDIO;
		file.type = File.EXTERNAL;
		file.mime = mime;
		file.size = size;

		return file;
	}

	public File createNewDocTrixFile(String mime, Long size) {
		File file = new File();
		file.directory = File.DIR_DOC;
		file.type = File.EXTERNAL;
		file.mime = mime;
		file.size = size;

		return file;
	}

	public boolean validate(FileItem item, Integer size) throws FileUploadException {
		if (item.getFieldName().equals("contents") || item.getFieldName().equals("file")) {
			if (item.getSize() > size) {
				throw new FileUploadException("Maximum file size is 16MB");
			}

			return true;
		}
		return false;
	}

	private VideoInternal getOriginalVideo(String mime, java.io.File originalFile, VideoFile videoFile, String name) throws IOException {
		VideoInternal originalVideo = new VideoInternal();
		originalVideo.file = createNewVideoTrixFile(mime, originalFile.length());
		originalVideo.file.name = originalVideo.title = name;

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(videoFile.hash));
		if (existingFile != null) {
			originalVideo.file = existingFile;
		}

		String hash = null;
		if (originalVideo.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null, originalVideo
					.file.mime, false, File.DIR_VIDEO);
		}

		if (originalVideo.file.hash == null || originalVideo.file.hash.isEmpty()) {
			originalVideo.file.hash = hash;
		}

		return originalVideo;
	}

	private Video getVideo(java.io.File originalFile) throws Exception {
		VideoFile videoFile;
		File existingFile;
		VideoInternal video = new VideoInternal();
		video.file = createNewVideoTrixFile("video/mp4", originalFile.length());

		java.io.File newFile = FileUtil.createNewTempFile();
		videoFile = new VideoFile(newFile, FileUtil.getHash(new FileInputStream(originalFile)));

		existingFile = fileRepository.findOne(QFile.file.hash.eq(video.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			video.file = existingFile;
		} else {
			amazonCloudService.uploadPublicFile(videoFile.file, videoFile.file.length(), videoFile.hash, null, video
					.file.mime, false, File.DIR_VIDEO);
		}

		return video;
	}

	private AudioInternal getOriginalAudio(String mime, java.io.File originalFile, AudioFile audioFile, String name) throws IOException {
		AudioInternal originalAudio = new AudioInternal();
		originalAudio.file = createNewVideoTrixFile(mime, originalFile.length());
		originalAudio.file.name = originalAudio.title = name;

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(audioFile.hash));
		if (existingFile != null) {
			originalAudio.file = existingFile;
		}

		String hash = null;
		if (originalAudio.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null,
					originalAudio.file.mime, false, File.DIR_AUDIO);
		}

		if (originalAudio.file.hash == null || originalAudio.file.hash.isEmpty()) {
			originalAudio.file.hash = hash;
		}

		return originalAudio;
	}

	private Audio getAudio(java.io.File originalFile) throws Exception {
		AudioFile audioFile;
		File existingFile;
		AudioInternal audioInternal = new AudioInternal();
		audioInternal.file = createNewVideoTrixFile("audio/x-mpeg-3", originalFile.length());

		java.io.File newFile = FileUtil.createNewTempFile();
		audioFile = new AudioFile(newFile, FileUtil.getHash(new FileInputStream(originalFile)));

		existingFile = fileRepository.findOne(QFile.file.hash.eq(audioInternal.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			audioInternal.file = existingFile;
		} else {
			amazonCloudService.uploadPublicFile(audioFile.file, audioFile.file.length(), audioFile.hash, null, audioInternal
					.file.mime, false, File.DIR_AUDIO);
		}

		return audioInternal;
	}

	private DocumentInternal getOriginalDocument(String mime, java.io.File originalFile, DocumentFile documentFile,
												 String name) throws IOException {
		DocumentInternal originalDocument = new DocumentInternal();
		originalDocument.file = createNewVideoTrixFile(mime, originalFile.length());
		originalDocument.file.name = originalDocument.title = name;

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(documentFile.hash));
		if (existingFile != null) {
			originalDocument.file = existingFile;
		}

		String hash = null;
		if (originalDocument.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null, originalDocument.file.mime, false, File.DIR_DOC);
		}

		if (originalDocument.file.hash == null || originalDocument.file.hash.isEmpty()) {
			originalDocument.file.hash = hash;
		}

		return originalDocument;
	}

	private Document getDocument(Callable<VideoFile> callable, java.io.File originalFile) throws Exception {
		DocumentFile documentFile;
		File existingFile;
		DocumentInternal documentInternal = new DocumentInternal();
		documentInternal.file = createNewVideoTrixFile("video/mp4", originalFile.length());

		java.io.File newFile = FileUtil.createNewTempFile();
		documentFile = new DocumentFile(newFile, FileUtil.getHash(new FileInputStream(originalFile)));

		existingFile = fileRepository.findOne(QFile.file.hash.eq(documentInternal.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			documentInternal.file = existingFile;
		} else {
			amazonCloudService.uploadPublicFile(documentFile.file, documentFile.file.length(), documentFile.hash, null, documentInternal
					.file.mime, false, File.DIR_DOC);
		}

		return documentInternal;
	}

	public static class FileMeta {
		public List<String> metadata;
	}

	public FileMeta extractMetadata(String fileName, FileInputStream fis) throws Throwable{
		/* extract file metadata and content */
		try{
			BodyContentHandler contenthandler = new BodyContentHandler(-1);
			//DefaultHandler contenthandler = new DefaultHandler();
			Metadata metadata = new Metadata();
			//			metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);
			Parser parser = new AutoDetectParser();
			parser.parse(fis, contenthandler, metadata,new ParseContext());
			String contentType =  metadata.get(Metadata.CONTENT_TYPE);
			if (StringUtils.isEmpty(contentType)) contentType="application/octet-stream";

			HashMap<String,Object> extractedMetaData = new HashMap<String,Object>();
			for (String key:metadata.names()){
				try{
					if (metadata.isMultiValued(key)){
						//							if (Logger.isDebugEnabled()) Logger.debug(key + ": ");
						for (String value: metadata.getValues(key)){
//							if (Logger.isDebugEnabled()) Logger.debug("   " + value);
						}
						extractedMetaData.put(key.replace(":", "_").replace(" ", "_").trim(), Arrays.asList(metadata.getValues(key)));
					}else{
						//							if (Logger.isDebugEnabled()) Logger.debug(key + ": " + metadata.get(key));
						extractedMetaData.put(key.replace(":", "_").replace(" ", "_").trim(), metadata.get(key));
					}
				}catch(Throwable e){
					Logger.warn("Unable to extract metadata for file " + fileName + ", key " + key);
				}
			}

			FileMeta fileMeta = new FileMeta();
			if(extractedMetaData.size() > 0){
				ArrayList<String> meta = new ArrayList<String>();
				for (Map.Entry<String, Object> entry : extractedMetaData.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					meta.add(key + " : " + value);
				}
				fileMeta.metadata = meta;
			}

			return fileMeta;
		}catch ( JsonProcessingException e) {
			//		    		throw new Exception ("Error parsing acl field. HINTS: is it a valid JSON string?", e);
			throw new BadRequestException("Error parsing json");
		}
	}
}
