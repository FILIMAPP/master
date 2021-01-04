package com.isoftinc.film.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.isoftinc.film.R
import com.isoftinc.film.adapter.DatailLatestAdapter
import com.isoftinc.film.adapter.EpisodeAdapter
import com.isoftinc.film.databinding.FragmentDetailBinding
import com.isoftinc.film.model.VideoModel
import com.isoftinc.film.util.ApplicationLoader
import com.isoftinc.film.util.BaseFragment
import com.isoftinc.film.util.ShowingImage
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util

class DetailFragment : BaseFragment() {
    var binding : FragmentDetailBinding? = null
    var isMovies :Boolean? = null
    var videoModel : VideoModel? = null
    private var cacheDataSourceFactory: CacheDataSourceFactory? = null
     var simpleExoPlayer: SimpleExoPlayer? = null
    private var simpleCache: SimpleCache? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getActivity()!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        binding = FragmentDetailBinding.bind(inflater.inflate(R.layout.fragment_detail, container, false))
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState != null){
            isMovies = savedInstanceState.getBoolean("isMovies")
        }

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.VISIBLE
        binding!!.toolbar.ibBack.visibility = View.GONE
        binding!!.toolbar.ibSearch.setOnClickListener {
            activity.replaceFragment(SearchFragment())
        }


        if(isMovies!!){
            setUI()
            binding!!.latest.visibility = View.VISIBLE
            val gridLayoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            binding!!.verticalRecycler.layoutManager = gridLayoutManager
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    // 2 column size for first row
                    return 1
                }
            }
            binding!!.verticalRecycler.adapter = DatailLatestAdapter()
        }else{
            binding!!.latest.visibility = View.GONE
            val layoutManager  = LinearLayoutManager(activity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            binding!!.verticalRecycler.layoutManager = layoutManager
            binding!!.verticalRecycler.adapter = EpisodeAdapter()

        }

        initPlayer()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isMovies", isMovies!!)
    }





    override fun onResume() {
        super.onResume()

      //  restartVideo()
    }

    override fun onPause() {
        super.onPause()
     //   binding!!.ibwatchMovie.visibility = View.VISIBLE
        pauseVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    companion object{

        fun newInstance(isMovies : Boolean, videoModel : VideoModel?) = DetailFragment().apply {
            this.isMovies = isMovies
            this.videoModel = videoModel

        }
    }

    fun setUI(){
        if(videoModel != null){
            ShowingImage.showImage(activity,videoModel!!.video!![0].thumbnail,binding!!.image)
            binding!!.tvdirector.text = videoModel!!.director
            binding!!.tvstarring.text = videoModel!!.starring
            binding!!.tvdescription.text = videoModel!!.description

            binding!!.tvwatch.setOnClickListener {
             //   binding!!.videoPlayer.visibility = View.VISIBLE
              //  binding!!.ibwatchMovie.visibility = View.GONE
                prepareMedia(Uri.parse(videoModel!!.video!![0].trailer[0].url))
            }

            binding!!.ibwatchMovie.setOnClickListener {
              //  binding!!.videoPlayer.visibility = View.VISIBLE
               // binding!!.ibwatchMovie.visibility = View.GONE
                prepareMedia(Uri.parse(videoModel!!.video!![0].main[0].url))
            }

            binding!!.btWatch.setOnClickListener {
               // binding!!.videoPlayer.visibility = View.VISIBLE
              //  binding!!.ibwatchMovie.visibility = View.GONE
                prepareMedia(Uri.parse(videoModel!!.video!![0].main[0].url))
            }

           /* binding!!.btFull.setOnClickListener {
               getActivity()!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }*/
        }

    }


    private fun initPlayer() {
        simpleCache = ApplicationLoader.simpleCache
        val simplePlayer = getPlayer()
      //  binding!!.videoPlayer.player = simplePlayer


    }

    private fun getPlayer(): SimpleExoPlayer? {
        if (simpleExoPlayer == null) {
            prepareVideoPlayer()
        }
        return simpleExoPlayer
    }

    private fun prepareVideoPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
        cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    context,
                    "exo"
                )
            )
        )

    }

    private fun prepareMedia(linkUrl: Uri) {


        val mediaSource = buildMediaSource(linkUrl)

        simpleExoPlayer?.prepare(mediaSource, true, true)
        simpleExoPlayer?.playWhenReady = true
      //  binding!!.videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        simpleExoPlayer?.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {


                        binding!!.image.animate()
                            .alpha(100f)
                            .setDuration(500)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    binding!!.image.visibility = View.GONE
                                }
                            })


                    }
                    Player.STATE_ENDED -> {
                        binding!!.ibwatchMovie.visibility = View.VISIBLE
                    }
                }
            }
        })
    }



    private fun buildMediaSource(uri: Uri): MediaSource? {
        return buildMediaSource(uri, null)
    }

    private fun buildMediaSource(uri: Uri, overrideExtension: String?): MediaSource? {
        @C.ContentType val type = Util.inferContentType(uri, overrideExtension)
        return when (type) {
            C.TYPE_DASH -> DashMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_OTHER -> ExtractorMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(uri)
            else -> {
                throw IllegalStateException("Unsupported type: $type")
            }
        }
    }

    private fun restartVideo() {

        if (simpleExoPlayer == null) {
            if(videoModel != null )
                prepareMedia(Uri.parse(videoModel!!.video!![0].trailer[0].url))
        } else {
            simpleExoPlayer?.playWhenReady = true
        }
    }

    private fun pauseVideo() {
        simpleExoPlayer?.playWhenReady = false
    }

    private fun releasePlayer() {
        simpleExoPlayer?.stop(true)
        simpleExoPlayer?.release()
    }


}