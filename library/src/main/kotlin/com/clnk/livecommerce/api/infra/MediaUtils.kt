package com.clnk.livecommerce.api.infra

import com.amazonaws.services.s3.AmazonS3
import com.clnk.livecommerce.api.config.AwsAttributes

import mu.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

private val logger = KotlinLogging.logger {}

@Component
class MediaUtils @Autowired constructor(
    private val props: AwsAttributes,
    private val amazonS3Client: AmazonS3,

    ) {
    fun getMediaInfo(file: MultipartFile, bucketPath: String): MediaInfo {
        val originalName = file.originalFilename!!
        val imageExt = getImageExtention(originalName)
        val modifyName = getModifyName(imageExt)
        val tempFile = File(props.tempFilePath + modifyName)
        logger.debug { "]-----] MediaUtils::getMediaInfo.originalFileName [-----[ $originalName" }
        file.transferTo(tempFile)
        if (isImage(tempFile)) {
            upload(bucketPath, tempFile)
        }
        return MediaInfo(
            originalName = originalName,
            modifyName = modifyName,
            imageExt = imageExt,
            fullPath = "${props.bucketUrl}/${bucketPath}/${modifyName}"
        )
    }

    fun getMediaInfo(file: FilePart, bucketPath: String): MediaInfo {
        val originalName = file.filename()
        val imageExt = getImageExtention(originalName)
        val modifyName = getModifyName(imageExt)
        val tempFile = File(props.tempFilePath + modifyName)
        logger.debug { "]-----] MediaUtils::getMediaInfo.originalFileName [-----[ $originalName" }
        file.transferTo(tempFile)
        logger.debug { "]-----] MediaUtils::getMediaInfo.tempFile [-----[ $tempFile" }
        if (isImage(tempFile)) {
            upload(bucketPath, tempFile)
        }
        return MediaInfo(
            originalName = originalName,
            modifyName = modifyName,
            imageExt = imageExt,
            fullPath = "${props.bucketUrl}/${bucketPath}/${modifyName}"
        )
    }

    fun getMediaInfoWebcam(file: FilePart, bucketPath: String): MediaInfo {
        val originalName = file.filename()
        val imageExt = "webm"
        val modifyName = getModifyName(imageExt)
        val tempFile = File(props.tempFilePath + modifyName)
        logger.debug { "]-----] MediaUtils::getMediaInfo.originalFileName [-----[ $originalName" }
        file.transferTo(tempFile)
        logger.debug { "]-----] MediaUtils::getMediaInfo.tempFile [-----[ $tempFile" }

        upload(bucketPath, tempFile)

        return MediaInfo(
            originalName = originalName,
            modifyName = modifyName,
            imageExt = imageExt,
            fullPath = "${props.bucketUrl}/${bucketPath}/${modifyName}"
        )
    }

    @Throws(IOException::class)
    private fun isImage(file: File): Boolean {
        return ImageIO.read(file) != null
    }

    private fun getImageExtention(filename: String): String {
        val index = filename.lastIndexOf(".")
        return filename.substring(index + 1)
    }

    private fun getModifyName(imageExt: String): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + RandomStringUtils.randomNumeric(10) + "." + imageExt
    }

    @Throws(InterruptedException::class)
    fun uploadMultiple(bucketPath: String, files: List<File>) {
        if (files.isNotEmpty()) {
            files.map {
                upload(bucketPath, it)
            }
        }
    }

    fun upload(bucketPath: String, file: File) = amazonS3Client.putObject("${props.bucketName}/${bucketPath}", file.name, file)

}

data class MediaInfo(
    var originalName: String = "",
    var modifyName: String = "",
    var imageExt: String = "",
    var bucketPath: String = "",
    var fullPath: String = "",
)