package com.su.service.ui.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.su.service.R
import com.su.service.model.youtube.YoutubeItem
import com.su.service.utils.Constants
import com.su.service.utils.DateGenerator
import kotlinx.android.synthetic.main.activity_detail_video.*

class DetailVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_DIALOG_REQUEST = 1
    private lateinit var video: YoutubeItem
    companion object{
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_video)
        official_player_view?.initialize(Constants.google_apikey, this)
        video = intent.getParcelableExtra(EXTRA_DATA)
        updateUI()
    }

    private fun updateUI() {
        tv_nama_video?.text = video.snippet?.title
        val tgl = DateGenerator.getTanggal("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEEE, dd MMMM yyyy", video.snippet?.publishedAt)
        tv_tgl_video?.text = tgl
        tv_deskripsi_video?.text = video.snippet?.description
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,youTubePlayer: YouTubePlayer,wasRestored: Boolean) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(video.id?.videoId)
        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider,youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                "There was an error initializing the YouTubePlayer (%1\$s)",
                youTubeInitializationResult.toString()
            )
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
