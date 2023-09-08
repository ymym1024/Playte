import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionSupport(private val activity: Activity, private val context: Context) {

    private val MULTIPLE_PERMISSIONS = 1023

    private val permissionsToCheck33 = arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    private val permissionsToCheckEtc = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private var permissionList = mutableListOf<String>()

    fun checkPermission(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            for (pm in permissionsToCheck33) {
                if (ContextCompat.checkSelfPermission(context, pm) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(pm)
                }
            }
            return permissionList.isEmpty()
        }else{
            for (pm in permissionsToCheckEtc) {
                if (ContextCompat.checkSelfPermission(context, pm) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(pm)
                }
            }
            return permissionList.isEmpty()
        }
    }

    fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(activity, permissionsToCheck33, MULTIPLE_PERMISSIONS)
        }else{
            ActivityCompat.requestPermissions(activity, permissionsToCheckEtc, MULTIPLE_PERMISSIONS)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray): Boolean {
        if (requestCode == MULTIPLE_PERMISSIONS && grantResults.isNotEmpty()) {
            for (g in grantResults) {
                if (g == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }
}
