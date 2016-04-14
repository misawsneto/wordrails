package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
	public FileService(AmazonCloudService amazonCloudService, FileRepository fileRepository, DocumentRepository documentRepository, AudioRepository audioRepository, VideoRepository videoRepository) {
		this.amazonCloudService = amazonCloudService;
		this.fileRepository = fileRepository;
		this.documentRepository = documentRepository;
		this.audioRepository = audioRepository;
		this.videoRepository = videoRepository;
	}

	private AmazonCloudService amazonCloudService;
	private FileRepository fileRepository;
	private DocumentRepository documentRepository;
	private AudioRepository audioRepository;
	private VideoRepository videoRepository;

	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@Transactional
	public Video createAndSaveNewVideo(String name, java.io.File originalFile, String mime) throws
			Exception {

		VideoFile videoFile = new VideoFile(originalFile, mime);
		VideoInternal newVideo = new VideoInternal();

		Video existingImage = videoRepository.findOne(QVideo.video.file.hash.eq(videoFile.hash));
		if (existingImage != null) {
			return existingImage;
		}

		Video originalVideo = getOriginalVideo(mime, originalFile, videoFile);

		return originalVideo;
	}

	@Transactional
	public Audio createAndSaveNewAudio(String name, java.io.File originalFile, String mime) throws
			Exception {

		AudioFile audioFile = new AudioFile(originalFile, mime);
		Audio newAudio = new Audio();

		Audio existingImage = audioRepository.findOne(QAudio.audio.file.hash.eq(audioFile.hash));
		if (existingImage != null) {
			return existingImage;
		}

		Audio originalAudio = getOriginalAudio(mime, originalFile, audioFile);

		return originalAudio;
	}

	@Transactional
	public Document createAndSaveNewDoc(String name, java.io.File originalFile, String mime) throws
			Exception {

		DocumentFile docFile = new DocumentFile(originalFile, mime);
		Document newDoc = new Document();

		Document existingImage = documentRepository.findOne(QDocument.document.file.hash.eq(docFile.hash));
		if (existingImage != null) {
			return existingImage;
		}

		Document originalDoc = getOriginalDocument(mime, originalFile, docFile);

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

	private Video getOriginalVideo(String mime, java.io.File originalFile, VideoFile videoFile) throws IOException {
		VideoInternal originalVideo = new VideoInternal();
		originalVideo.file = createNewVideoTrixFile(mime, originalFile.length());

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(videoFile.hash));
		if (existingFile != null) {
			originalVideo.file = existingFile;
		}

		String hash = null;
		if (originalVideo.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null, originalVideo
					.file.getExtension(), false, File.DIR_VIDEO);
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
		videoFile = new VideoFile(newFile, FileUtil.getHash(new FileInputStream(newFile)));

		existingFile = fileRepository.findOne(QFile.file.hash.eq(video.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			video.file = existingFile;
		} else {
			amazonCloudService.uploadPublicFile(videoFile.file, videoFile.file.length(), videoFile.hash, null, video
					.file.getExtension(), false, File.DIR_VIDEO);
		}

		return video;
	}

	private Audio getOriginalAudio(String mime, java.io.File originalFile, AudioFile audioFile) throws IOException {
		Audio originalAudio = new Audio();
		originalAudio.file = createNewVideoTrixFile(mime, originalFile.length());

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(audioFile.hash));
		if (existingFile != null) {
			originalAudio.file = existingFile;
		}

		String hash = null;
		if (originalAudio.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null,
					originalAudio.file.getExtension(), false, File.DIR_AUDIO);
		}

		if (originalAudio.file.hash == null || originalAudio.file.hash.isEmpty()) {
			originalAudio.file.hash = hash;
		}

		return originalAudio;
	}

	private Audio getAudio(java.io.File originalFile) throws Exception {
		AudioFile audioFile;
		File existingFile;
		Audio audio = new Audio();
		audio.file = createNewVideoTrixFile("audio/x-mpeg-3", originalFile.length());

		java.io.File newFile = FileUtil.createNewTempFile();
		audioFile = new AudioFile(newFile, FileUtil.getHash(new FileInputStream(newFile)));

		existingFile = fileRepository.findOne(QFile.file.hash.eq(audio.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			audio.file = existingFile;
		} else {
			amazonCloudService.uploadPublicFile(audioFile.file, audioFile.file.length(), audioFile.hash, null, audio
					.file.getExtension(), false, File.DIR_VIDEO);
		}

		return audio;
	}

	private Document getOriginalDocument(String mime, java.io.File originalFile, DocumentFile documentFile) throws
			IOException {
		Document originalDocument = new Document();
		originalDocument.file = createNewVideoTrixFile(mime, originalFile.length());

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(documentFile.hash));
		if (existingFile != null) {
			originalDocument.file = existingFile;
		}

		String hash = null;
		if (originalDocument.file.id == null) {
			hash = amazonCloudService.uploadPublicFile(originalFile, originalFile.length(), null, null, originalDocument.file.getExtension(), false, File.DIR_DOC);
		}

		if (originalDocument.file.hash == null || originalDocument.file.hash.isEmpty()) {
			originalDocument.file.hash = hash;
		}

		return originalDocument;
	}

	private Video getDocument(Callable<VideoFile> callable, java.io.File originalFile) throws Exception {
		VideoFile videoFile;
		File existingFile;
		VideoInternal video = new VideoInternal();
		video.file = createNewVideoTrixFile("video/mp4", originalFile.length());

		videoFile = callable.call();

		existingFile = fileRepository.findOne(QFile.file.hash.eq(videoFile.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			video.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(videoFile.file, videoFile.file.length(), videoFile.hash, null, video
					.file.getExtension(), false);
		}

		video.file.size = videoFile.file.length();
		video.file.hash = videoFile.hash;

		return video;
	}
}
