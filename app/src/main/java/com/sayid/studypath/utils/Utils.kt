package com.sayid.studypath.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private var isDatePickerShown = false
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String =
    android.icu.text
        .SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(Date())
private const val MAXIMAL_SIZE = 1000000

fun Context.startActivityNoAnimation(intent: Intent) {
    val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
    startActivity(intent, options.toBundle())
}

fun showDatePicker(
    fragmentManager: FragmentManager,
    onDateSelected: (String) -> Unit,
) {
    if (isDatePickerShown) return

    isDatePickerShown = true

    val constraintsBuilder =
        CalendarConstraints
            .Builder()
            .setValidator(DateValidatorPointBackward.now())

    val datePicker =
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Updated format
        val formattedDate = dateFormatter.format(Date(selection))
        onDateSelected(formattedDate)
    }

    datePicker.addOnDismissListener {
        isDatePickerShown = false
    }

    datePicker.show(fragmentManager, "DATE_PICKER")
}

fun showToast(
    context: Context,
    message: String,
    isShort: Boolean = true,
) {
    Toast.makeText(context, message, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}

fun saveBitmapToFixedCache(
    context: Context,
    bitmap: Bitmap,
): Uri {
    val fileName = "cached_image.png"
    val file = File(context.cacheDir, fileName)
    FileOutputStream(file).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    }
    return Uri.fromFile(file)
}

fun saveBitmapToCache(
    context: Context,
    bitmap: Bitmap,
): Uri {
    val file = File(context.cacheDir, "image_$timeStamp.png")
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return Uri.fromFile(file)
}

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun drawableToCacheUri(
    context: Context,
    drawableRes: Int,
): Uri? {
    val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return null
    val bitmap =
        if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: 1
            val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: 1
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                val canvas = Canvas(this)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
            }
        }

    return saveBitmapToCache(context, bitmap)
}

fun uriToFile(
    imageUri: Uri,
    context: Context,
): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation =
        ExifInterface(file).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED,
        )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(
    source: Bitmap,
    angle: Float,
): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source,
        0,
        0,
        source.width,
        source.height,
        matrix,
        true,
    )
}
