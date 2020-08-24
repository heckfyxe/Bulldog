package com.heckfyxe.bulldog

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.fondesa.kpermissions.isGranted
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine

private const val PERMISSION_REQ_ID_RECORD_AUDIO: Int = 0

class MainActivity : AppCompatActivity() {
    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        override fun onUserOffline(uid: Int, reason: Int) {
//            runOnUiThread { onRemoteUserLeft() }
            toast("User left")
        }

        // Listen for the onUserMuterAudio callback.
        // This callback occurs when a remote user stops sending the audio stream.
        override fun onUserMuteAudio(uid: Int, muted: Boolean) {
//            runOnUiThread { onRemoteUserVoiceMuted(uid, muted)}
        }

        override fun onJoinChannelSuccess(p0: String?, p1: Int, p2: Int) {
            toast("Success join")
        }

        override fun onUserJoined(p0: Int, p1: Int) {
            toast("User join")
        }

        override fun onError(p0: Int) {
            toast("Error")
        }

        private fun toast(text: String) {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsBuilder(Manifest.permission.RECORD_AUDIO).build().send {
            if (it.first().isGranted())
                initAgoraEngineAndJoinChannel()
        }
    }

    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        joinChannel()
    }

    // Call the create method to initialize RtcEngine.
    private fun initializeAgoraEngine() {
        try {
            mRtcEngine =
                RtcEngine.create(baseContext, BuildConfig.API_KEY, mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(MainActivity::class.java.simpleName, Log.getStackTraceString(e))

            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                    e
                )
            )
        }
    }

    private fun joinChannel() {

        // Call the joinChannel method to join a channel.
        // The uid is not specified. The SDK will assign one automatically.
        mRtcEngine!!.joinChannel(null, "demoChannel1", "Extra Optional Data", 0)
    }

}