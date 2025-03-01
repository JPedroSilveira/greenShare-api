package com.greenshare.helpers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import com.greenshare.entity.interfaces.PhotogenicEntity;
import com.greenshare.enumeration.PhotoType;
import com.greenshare.exception.DirectoryException;

/**
 * @author joao.silva
 */
public class ImageHelper {

	/*
	 * To use ImageHelper
	 *
	 * - Create a new value in Enum PhotoType Where the 'directoryName' will be used
	 * to name the folder where the images are saved. - Set a static final attribute
	 * called PHOTO_TYPE that references the Enum of your Entity Class on the Enum
	 * PhotoType. - Extends Class AbstractPhotogenicClass on your Entity giving his
	 * type and set the super(PHOTO_TYPE) contructor with the PHOTO_TYPE - The
	 * constructor of a newer entity is super(PHOTO_TYPE, true) and the constructor
	 * of a existing entity is super(PHOTO_TYPE) - Create the routes on
	 * ImageUploadController and implement they on ImageUploadControllerImpl, on
	 * route method you can call the methods saveImage and getImage. - On
	 * PhotogenicServiceImpl you will need to add a conditional to save the hasImage
	 * attribute using your entity's repository. - The image will be saved based on
	 * the entity id and only one image per entity can be saved. - On save a new
	 * image the old is deleted.
	 * 
	 */
	private final static String IMAGES_DIRECTORY = "images/";

	private final static int MAX_FILE_SIZE = 5000000;

	private MultipartFile multiPartFile;

	private PhotogenicEntity entity;

	private PhotoType photoType;

	private File imageDirectory;

	private File image;

	private String imageFormat;

	private Long id;

	public ImageHelper(PhotogenicEntity entity) throws DirectoryException {
		this.entity = entity;
		this.id = this.entity.getId();
		this.photoType = this.entity.getPhotoType();
		this.imageDirectory = getOrTryCreateImageDirectory();
	}

	public boolean save(MultipartFile multiPartFile) throws IOException {
		this.multiPartFile = multiPartFile;
		this.imageFormat = this.multiPartFile.getContentType()
				.substring(this.multiPartFile.getContentType().indexOf('/') + 1);
		if (isValidImage() && cleanDirectory()) {
			this.image = getImageFile();
			FileOutputStream imageOutput = new FileOutputStream(this.image);
			imageOutput.write(multiPartFile.getBytes());
			imageOutput.close();
			return true;
		}
		return false;
	}
	
	public boolean save(byte[] file) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(file);
		BufferedImage image = ImageIO.read(bais);
		File directory = getDirectoryFile();
		ImageIO.write(image, "jpg", directory);
		return true;
	}

	public String getImage() throws IOException, JSONException {
		File[] files = this.imageDirectory.listFiles();
		this.imageFormat = files[0].getName().substring(files[0].getName().indexOf(".") + 1);
		File imageFile = getImageFile();
		if (imageFile != null && imageFile.isFile()) {
			FileInputStream imageInFile = new FileInputStream(imageFile);
            byte imageData[] = new byte[(int) imageFile.length()];
            imageInFile.read(imageData);
            imageInFile.close();
            return Base64.getEncoder().encodeToString(imageData);
		}
		return null;
	}

	private Boolean isValidImage() {
		return this.multiPartFile != null && this.multiPartFile.getSize() <= MAX_FILE_SIZE
				&& (this.imageFormat.equalsIgnoreCase("png") || this.imageFormat.equalsIgnoreCase("jpg")
						|| this.imageFormat.equals("jpeg"));
	}

	private File getImageFile() {
		return new File(imageDirectory.getPath().concat("/").concat(this.photoType.getDirectoryName()).concat(".")
				.concat(this.imageFormat));
	}
	
	private File getDirectoryFile() {
		return new File(imageDirectory.getPath().concat("/").concat(this.photoType.getDirectoryName()));
	}

	private File getOrTryCreateImageDirectory() throws DirectoryException {
		char[] idInCharArray = this.id.toString().toCharArray();
		String idDirectory = getIdDirectory(idInCharArray);
		File file = new File(
				IMAGES_DIRECTORY.concat("/").concat(this.photoType.getDirectoryName()).concat("/").concat(idDirectory));
		if (!file.exists() && !file.mkdirs()) {
			throw new DirectoryException("Falha ao encontrar diretório da imagem.");
		}
		return file;
	}

	private String getIdDirectory(char[] idInCharArray) {
		StringBuilder idDirectory = new StringBuilder();

		for (char number : idInCharArray) {
			idDirectory.append(number).append("/");
		}

		return idDirectory.toString();
	}

	private Boolean cleanDirectory() {
		if (this.imageDirectory.isDirectory()) {
			File[] files = this.imageDirectory.listFiles();
			for (File file : files) {
				Boolean fileIsDeleted = file.delete();
				if (!fileIsDeleted) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
