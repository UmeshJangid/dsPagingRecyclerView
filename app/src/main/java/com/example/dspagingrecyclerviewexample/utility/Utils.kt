package com.example.dspagingrecyclerviewexample.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.app.admin.DevicePolicyManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.dspagingrecyclerviewexample.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by : Umesh Jangid
 * Date : 31 March 2020
 * This class is util for my complete projects any thing which i need in globally, It should be here.
 */
object Utils {
    private const val EMAIL_PATTERN =
        "^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$"
    private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[!@_*#\$%^&+=])(?=\\S+\$).{6,}\$"
    private var toast: Toast? = null
    private val progressDialog: ProgressDialog? = null
    private var snackbar: Snackbar? = null
    val OOPS_STRING: String = "Oops! Something went wrong, Try Again!"
    var dialog: Dialog? = null


    fun hideDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun lockDevice(devicePolicyManager: DevicePolicyManager, componentName: ComponentName) {
        Log.e("Lock", "Iam Here")
        if (devicePolicyManager != null && componentName != null) {
            var active = devicePolicyManager.isAdminActive(componentName)
            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Log.e("Lock", "Enable Admin Lock")
                // showToast("You need to enable the Admin Device Features")
            }
        } else {
            Log.e("Lock", "devicePolicyManager is null")
        }
    }


    fun getOSName(): String {
        var osName = ""
        try {
            val fields = Build.VERSION_CODES::class.java.fields
            var codeName = "UNKNOWN"
            fields.filter { it.getInt(Build.VERSION_CODES::class) == Build.VERSION.SDK_INT }
                .forEach { codeName = it.name }
            osName = codeName
        } catch (e: Exception) {

        }
        return osName
    }

    /* IMEI  */


    /*Get Device Name */
    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer).toString() + " " + model
        }
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getSerialNumber(context: Context): String {
        var serialNumber = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serialNumber = Build.getSerial()
            } else {
                serialNumber = Build.SERIAL
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                serialNumber = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        return serialNumber
    }


    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Activity): String? {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return ""
        }
        var deviceId: String = ""
        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                mTelephony.deviceId
            } else {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        return deviceId
    }


    private fun capitalize(s: String?): String? {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }


    /**
     * This method return date in this format :  2015/01/02 23:14:05
     * */
    fun getCurrentTimeInFormat(): String {
        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val strDate = dateFormat.format(c.time)
        return strDate
    }


    fun isValidPassword(target: String): Boolean {
        return (target.matches(Regex(PASSWORD_REGEX)))
    }

    /* Checks if external storage is available for read and write */
    val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    /* Checks if external storage is available to at least read */
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    fun showDialog(context: Context) {
        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
            dialog = null
        }
        dialog = Dialog(context, R.style.styleDialogTransparent)
        dialog!!.setContentView(R.layout.layout_dialog_progress)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    @JvmStatic
    fun showMessageDialog(message: String, context: Context) {
        message?.let {
            context?.let { context ->
                androidx.appcompat.app.AlertDialog.Builder(context).setMessage(message)
                    .setNeutralButton(R.string.ok, { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                    }).create().show()
            }
        }
    }

    @JvmStatic
    fun showToast(context: Context, message: String) {
        toast(context, message, false)// false mean DURATION_SHORT
    }

    fun toast(context: Context, message: String, durationShort: Boolean) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(
            context,
            message,
            if (durationShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        )
        toast!!.show()
    }

    fun showSnackBar(
        view: View,
        message: String,
        actionName1: String?,
        listener1: View.OnClickListener?,
        duration: Int
    ) {
        showSnackBar(view, message, actionName1, listener1, null, null, duration)
    }

    fun showSnackBar(
        view: View,
        message: String,
        actionName1: String?,
        listener1: View.OnClickListener?,
        actionName2: String?,
        listener2: View.OnClickListener?,
        duration: Int
    ) {
        if (snackbar != null) {
            snackbar!!.dismiss()
        }
        snackbar = Snackbar.make(view, message, duration)
        val snackbarView = snackbar!!.getView()
        snackbarView.setBackgroundColor(Color.parseColor("#ec3338"))
        //  snackbar.(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
        if (actionName1 != null && listener1 != null) {
            snackbar!!.setAction(actionName1, listener1)
        }
        if (actionName2 != null && listener2 != null) {
            snackbar!!.setAction(actionName2, listener2)
        }
        snackbar!!.show()
    }

    fun dismissSnackbar() {
        if (snackbar != null) {
            snackbar!!.dismiss()
        }
    }


    fun getImageUri(inImage: Bitmap, context: Context): Uri {
        // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    // Returns true if external storage for photos is available
    private fun isExternalStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }


    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }


    fun getFilename(context: Context): String {
        val mediaStorageDir =
            File(Environment.getExternalStorageDirectory().toString() + "/ProjectName")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }
        val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".jpg"
        return mediaStorageDir.absolutePath + "/" + mImageName
    }


    fun commaSeparatedStringToArrayList(commaSeparated: String?): ArrayList<String>? {
        var list: ArrayList<String>? = null
        if (commaSeparated != null && commaSeparated.isNotEmpty()) {
            list =
                ArrayList(Arrays.asList(*commaSeparated.split(",".toRegex()).dropLastWhile { it.isNotEmpty() }.toTypedArray()))
        }
        return list
    }

    fun arrayListToCommaSeparatedString(list: ArrayList<String>): String? {
        var s: String? = null
        if (list.size > 0) {
            s = TextUtils.join(",", list.toTypedArray())
        }
        return s
    }

 /*   @JvmStatic
    fun shakeView(view: View, context: Context) {
        val animShake = AnimationUtils.loadAnimation(context, R.anim.animation)
        view.startAnimation(animShake)
    }


    @JvmStatic
    fun shakeItemView(view: View, context: Context) {
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)
        val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, 10))
        } else {
            vibrator.vibrate(100)
        }

        view.startAnimation(animShake)
    }
*/


    /* Go to App Settings */

    fun goToAppSetting(context: Context) {
        val builder = AlertDialog.Builder(context)
            .setMessage(
                "For proper functioning of app you need to provide all permisson to the app." +
                        "this won't harm your phone or your data." +
                        "For that go to Enable Now -> Permissions"
            )
            .setPositiveButton("Enable Now") { dialog, which ->
                dialog.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
        builder.create().show()
    }


    /**
     * @param currentdateFormat = "What your date format is pass in this variable"
     * @param requireFormat ="Format you require"
     * @param dateStr = "Your date in string format"
     * */
    /* fun changeDateToFormat(currentdateFormat: String, requireFormat: String, dateStr: String): String {
         var result: String? = null
         if (dateStr.isNullOrEmpty()) {
             return result!!
         }
         val formatterOld = SimpleDateFormat(currentdateFormat, Locale.getDefault())
         val formatterNew = SimpleDateFormat(requireFormat, Locale.getDefault())
         var date: Date? = null
         try {
             date = formatterOld.parse(dateStr)
         } catch (e: ParseException) {
             e.printStackTrace()
         }

         if (date != null) {
             result = getFormattedDate(date)
         }
         return result!!

     }*/


    /**
     * Is network connected available boolean.
     *
     * @param context the context
     * @return the boolean
     */
    fun isNetworkConnectedAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }


    /**
     * Is valid email boolean.
     * @param email the email
     * @return the boolean
     */
    fun isValidEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) false else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Is valid phone number boolean.
     *
     * @param number the number
     * @return the boolean
     */
    fun isValidPhoneNumber(number: String): Boolean {
        return if (TextUtils.isEmpty(number)) false else Patterns.PHONE.matcher(number).matches()
//        return ((number.length() == 10 && !number.startsWith("0", 0)) || number.length() > 10)
        // && Patterns.PHONE.matcher(number.trim()).matches();
    }

    /**
     * Close key board.
     *
     * @param context the context
     */
    fun closeKeyBoard(context: Context) {
        if (context is Activity) {
            val view = context.currentFocus
            if (view != null) {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    /**
     * Close key board.
     *
     * @param context the context
     * @param view    the view
     */
    fun closeKeyBoard(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Gets rounded corner bitmap.
     *
     * @param bitmap the bitmap
     * @param pixels the pixels
     * @return the rounded corner bitmap
     */
    fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap
                .height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = pixels.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    /**
     * Gets bitmap from drawable.
     *
     * @param drawable the drawable
     * @return the bitmap from drawable
     */
    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap
            val COLORDRAWABLE_DIMENSION = 2
            val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
            if (drawable is ColorDrawable) {
                bitmap = Bitmap.createBitmap(
                    COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION,
                    BITMAP_CONFIG
                )
            } else {
                bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    BITMAP_CONFIG
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    fun convertSpToPx(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun convertDpToPx(context: Context, dps: Int): Int {
        val metrics = DisplayMetrics()
        if (context is Activity) {
            context.windowManager.defaultDisplay.getMetrics(metrics)
            return (metrics.density * dps).toInt()
        } else
            return 0
    }




}
