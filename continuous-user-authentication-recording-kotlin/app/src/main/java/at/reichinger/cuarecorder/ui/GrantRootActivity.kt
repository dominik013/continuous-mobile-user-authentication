package at.reichinger.cuarecorder.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.reichinger.cuarecorder.R
import at.reichinger.cuarecorder.service.data.Task
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GrantRootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grant_root)
    }

    fun onGrantRootClick(view: View) {
        GlobalScope.launch {
            Shell.su("ls").exec()

            if (Shell.rootAccess()) {
                Task.Settings.setTaskDone(
                    this@GrantRootActivity,
                    Task.ID.GRANT_ROOT_ACCESS
                )
                finish()
            }
        }
    }
}
