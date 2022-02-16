package com.su.service.ui.video

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.Provider
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.YouTubePlayerView
import com.su.service.R
import com.su.service.utils.Constants
import kotlinx.android.synthetic.main.fragment_video_player.*


class VideoPlayerFragment() : BottomSheetDialogFragment(), YouTubePlayer.OnInitializedListener {
    companion object{
        const val EXTRA_VIDEO_ID = "extra_video_id"
        const val EXTRA_TEXT = "extra_text"
    }
    private val RECOVERY_DIALOG_REQUEST = 1
    private var videoId: String? = null
    private var judul: String? = null
    private lateinit var youtubeView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_player,container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_video_detail_judul?.text = judul

        videoId = arguments?.getString(EXTRA_VIDEO_ID, "")
        judul = arguments?.getString(EXTRA_TEXT,"")
        val youTubePlayerFragment =
            requireActivity().supportFragmentManager
                .findFragmentById(R.id.youtube_frag) as YouTubePlayerSupportFragment?
        //youTubePlayerFragment?.initialize(Constants.google_apikey, this)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youtubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if(!wasRestored){
            youtubePlayer?.cueVideo(videoId)
        }
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        if (youTubeInitializationResult?.isUserRecoverableError!!) {
            youTubeInitializationResult.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST)?.show()
        } else {
            val errorMessage = String.format(
                "There was an error initializing the YouTubePlayer (%1\$s)",
                youTubeInitializationResult.toString()
            )
            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.google_apikey, this)
        }
    }

    fun getYouTubePlayerProvider(): Provider {
        return youtubeView
    }
}


