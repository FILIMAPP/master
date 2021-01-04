
package com.isoftinc.film.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.isoftinc.film.R
import com.isoftinc.film.adapter.DatailLatestAdapter
import com.isoftinc.film.adapter.EpisodeAdapter
import com.isoftinc.film.adapter.SeasonSpinnerAdapter
import com.isoftinc.film.databinding.FragmentDetailBinding
import com.isoftinc.film.dialog.CastingDialog
import com.isoftinc.film.dialog.LanguageDialog
import com.isoftinc.film.dialog.SubscriptionBottomSheetDialog
import com.isoftinc.film.fragment.RegisterFragment
import com.isoftinc.film.model.*
import com.isoftinc.film.retrofit.ApiClient
import com.isoftinc.film.retrofit.ApiInterface
import com.isoftinc.film.util.*
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class DetailActivity : BaseActivity(), PaymentResultListener {

    var binding : FragmentDetailBinding? = null
    private var cacheDataSourceFactory: CacheDataSourceFactory? = null
    var simpleExoPlayer: SimpleExoPlayer? = null
    private var simpleCache: SimpleCache? = null
    var url = ""
    val service: ApiClient? = ApiClient()
    var detailLatestAdapter : DatailLatestAdapter? = null
    var durationMillis : Long? = 0
    val handler = Handler()
    var quality : ArrayList<TrailerModel> = ArrayList()
    var    mobileVerificationResult : MobileVerificationResult? = null
    var preferenceStore : PreferenceStore? = null
    var subId : String? = null
    var isFirstTime = true
    var ismute = false
    var selectedQuality = ""

    var seriesModel : SeriesListModels ? = null
    var episodeAdapter : EpisodeAdapter? = null
    var episodeModel: SessionDetail? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_detail)
       window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val orientation =   resources.configuration.orientation
        if(orientation == 1){
            releasePlayer()
            binding!!.fullvideoPlayer.visibility = View.GONE
            binding!!.llDetail.visibility = View.VISIBLE
            binding!!.rlFulloptions.visibility = View.GONE
            initPlayer()
            preferenceStore = PreferenceStore(applicationContext)
            if(savedInstanceState != null){
                binding!!.progressCircular.visibility = View.GONE
                binding!!.image.visibility = View.GONE
                binding!!.ibwatchMovie.visibility = View.GONE
                binding!!.videoPlayer.visibility = View.VISIBLE
                 url = savedInstanceState.getString("url")!!
                durationMillis = savedInstanceState.getLong("durationmills")
                ismute = savedInstanceState.getBoolean("isMute")
                prepareMedia(Uri.parse(url), durationMillis!!)
            }
        }else if (orientation == 2){
            releasePlayer()
            initFullPlayer()
            preferenceStore = PreferenceStore(applicationContext)
            binding!!.fullvideoPlayer.visibility = View.VISIBLE
            binding!!.llDetail.visibility = View.GONE
            binding!!.progressCircular.visibility = View.GONE
            binding!!.rlFulloptions.visibility = View.VISIBLE
             url = savedInstanceState?.getString("url")!!
            durationMillis = savedInstanceState.getLong("durationmills")
            ismute = savedInstanceState.getBoolean("isMute")
            prepareMedia(Uri.parse(url), durationMillis!!)

        }
        val gson = Gson()
        val json = preferenceStore!!.getUserDetail()
        mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)

        if( isMovies != null && !isMovies!! )
            singleSeriesData()
        else
        singleDetailData()
        checkInternetConnection(this, binding!!.llDetail).execute()

        binding!!.toolbar.skip.visibility = View.GONE
        binding!!.toolbar.ibSearch.visibility = View.GONE
        binding!!.toolbar.ibBack.visibility = View.VISIBLE
        binding!!.toolbar.tvsubscribe.text = getText(R.string.subscribe)
        binding!!.toolbar.ibBack.setOnClickListener {
            finish()
        }

        if(mobileVerificationResult == null || mobileVerificationResult!!.subscription == null){
            binding!!.toolbar.tvsubscribe.visibility = View.VISIBLE

        }else{
            binding!!.toolbar.tvsubscribe.visibility = View.GONE
        }

        binding!!.toolbar.tvsubscribe.setOnClickListener {
            openSubscriptionDialog()
        }


        binding!!.btmute.setOnClickListener {
            if(ismute){
                ismute = false
                simpleExoPlayer!!.volume = 0f
                binding!!.btmute.setBackgroundResource(R.drawable.ic_baseline_volume_off_24)
            }else{
                ismute = true
                simpleExoPlayer!!.volume = 50f
                binding!!.btmute.setBackgroundResource(R.drawable.ic_baseline_volume_up_24)
            }
        }

        binding!!.btFullmute.setOnClickListener {
            if(ismute){
                ismute = false
                simpleExoPlayer!!.volume = 0f
                binding!!.btmute.setBackgroundResource(R.drawable.ic_baseline_volume_off_24)
            }else{
                ismute = true
                simpleExoPlayer!!.volume = 50f
                binding!!.btmute.setBackgroundResource(R.drawable.ic_baseline_volume_up_24)
            }
        }


    }

    companion object{
        var videoModel : VideoModel? = null
        var videoListModel : VideoListModel? = null
        var id = ""
        var isMovies :Boolean? = null
        var sessionId = ""
        var isSeries = false
        var episodeid = ""
        var isMovieUrl = false
        var detailModels :DetailModel? = null
        var language_id = ""
        fun newInstance(
            videoModels: VideoModel?,
            videoListModels: VideoListModel?,
            ids: String,
            isMovie: Boolean,
            sessionIds: String
        ) = DetailActivity().apply {
            videoModel = videoModels
            videoListModel = videoListModels
            id = ids
            isMovies = isMovie
            sessionId = sessionIds

        }
    }

    fun setUI(detailModel: DetailModel?, commonVideoList: ArrayList<CommonVideoModel>){
        if(detailModel != null){
            if( detailModel.type == "M"){
                binding!!.latest.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                detailLatestAdapter = DatailLatestAdapter()
                binding!!.verticalRecycler.layoutManager = layoutManager
                binding!!.verticalRecycler.adapter = detailLatestAdapter
                detailLatestAdapter!!.setDetailAdapter(this@DetailActivity, commonVideoList)
            }else{
                binding!!.latest.visibility = View.GONE
                val layoutManager  = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                binding!!.verticalRecycler.layoutManager = layoutManager
                binding!!.verticalRecycler.adapter = EpisodeAdapter()

            }
            ShowingImage.showImage(this, detailModel.video[0].poster, binding!!.image)
            binding!!.tvname.text = detailModel.name
            binding!!.tvdirector.text = detailModel.director
            binding!!.tvstarring.text = detailModel.starring
            binding!!.tvdescription.text = detailModel.description

            binding!!.tvwatch.setOnClickListener {
                isMovieUrl = false

                if(detailModel.video[0].trailer.size > 0){
                    binding!!.videoPlayer.visibility = View.VISIBLE
                    binding!!.image.animate()
                        .alpha(100f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                binding!!.image.visibility = View.GONE
                            }
                        })
                    url = detailModel.video[0].trailer[0].url
                    prepareMedia(Uri.parse(detailModel.video[0].trailer[0].url), 0)

                }else{
                    Toast.makeText(this, "Trailer Not Available", Toast.LENGTH_SHORT).show()
                }
                    }

            binding!!.ibwatchMovie.setOnClickListener {
                val gson = Gson()
                val json = preferenceStore!!.getUserDetail()
                mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
                if(preferenceStore!!.getUserDetail().isNullOrEmpty() ){
                    openSubscriptionDialog()
                }else if(mobileVerificationResult != null && mobileVerificationResult!!.subscription == null)
                    openSubscriptionDialog()
                else{
                    isMovieUrl = true
                    binding!!.ibwatchMovie.visibility = View.GONE
                    binding!!.videoPlayer.visibility = View.VISIBLE
                    binding!!.videoProgress.visibility = View.VISIBLE
                    binding!!.image.animate()
                        .alpha(100f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                binding!!.image.visibility = View.GONE
                            }
                        })
                    url = detailModel.video[0].main[0].url
                    prepareMedia(Uri.parse(detailModel.video[0].main[0].url), durationMillis!!)
                }

            }



            binding!!.btWatch.setOnClickListener {
                val gson = Gson()
                val json = preferenceStore!!.getUserDetail()
                mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
                if(preferenceStore!!.getUserDetail().isNullOrEmpty()){
                    openSubscriptionDialog()
                }else if(mobileVerificationResult != null && mobileVerificationResult!!.subscription == null)
                    openSubscriptionDialog()
                else{
                    isMovieUrl = true
                    binding!!.videoProgress.visibility = View.VISIBLE
                    binding!!.ibwatchMovie.visibility = View.GONE
                    binding!!.videoPlayer.visibility = View.VISIBLE
                    binding!!.image.animate()
                        .alpha(100f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                binding!!.image.visibility = View.GONE
                            }
                        })
                    url = detailModel.video[0].main[0].url
                    prepareMedia(Uri.parse(detailModel.video[0].main[0].url), durationMillis!!)
                }
            }

            binding!!.btFull.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            binding!!.ibExit.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            binding!!.quality.setOnClickListener {
                openDialog(detailModel.video, quality, true)
            }

            binding!!.Fullquality.setOnClickListener {
                openDialog(detailModel.video, quality,true)
            }

            binding!!.language.setOnClickListener {
                openDialog(detailModel.video, null, true)
            }

            binding!!.share.setOnClickListener {
                onShareClick()
            }

            binding!!.ibCast.setOnClickListener {
                openCastDialog()
            }

            binding!!.FullibCast.setOnClickListener {
                openCastDialog()
            }

        }

        binding!!.rlVideoPlay.setOnClickListener {
            binding!!.rloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rloptions.visibility = View.GONE
            }, 5000)
        }

        binding!!.fullvideoPlayer.setOnClickListener {
            binding!!.rlFulloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rlFulloptions.visibility = View.GONE
            }, 5000)
        }

        binding!!.videoPlayer.setOnClickListener {
            binding!!.rloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rloptions.visibility = View.GONE
            }, 5000)
        }


        Handler().postDelayed({
            binding!!.rloptions.visibility = View.GONE
            binding!!.rlFulloptions.visibility = View.GONE
        }, 5000)
    }

    private fun initPlayer() {
        simpleCache = ApplicationLoader.simpleCache
        val simplePlayer = getPlayer()
      binding!!.videoPlayer.player = simplePlayer


    }

   private fun initFullPlayer(){
       simpleCache = ApplicationLoader.simpleCache
       val simplePlayer = getPlayer()
       binding!!.fullvideoPlayer.player = simplePlayer
   }


    override fun onPause() {
        super.onPause()
        pauseVideo()
        if(durationMillis!! > 2000  && (durationMillis!!  < simpleExoPlayer!!.duration -   50000) && isMovieUrl)
        updateSession(false)
        else if(durationMillis!! > 2000  && (durationMillis!!  < simpleExoPlayer!!.duration -   10000) && isSeries)
            updateSession(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        releasePlayer()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("url", url)
        outState.putLong("durationmills", durationMillis!!)
        outState.putBoolean("isMute", ismute)
    }


    private fun getPlayer(): SimpleExoPlayer? {
        if (simpleExoPlayer == null) {
            prepareVideoPlayer()
        }
        return simpleExoPlayer
    }

    private fun prepareVideoPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        cacheDataSourceFactory = CacheDataSourceFactory(
            simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    this,
                    "exo"
                )
            )
        )

    }

    private fun prepareMedia(linkUrl: Uri, durationmills: Long) {


        val mediaSource = buildMediaSource(linkUrl)
        simpleExoPlayer?.prepare(mediaSource, true, true)
        simpleExoPlayer?.playWhenReady = true
        simpleExoPlayer!!.seekTo(durationmills)
        binding!!.fullvideoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        binding!!.videoPlayer.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        simpleExoPlayer?.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                updateProgressBar()
                when (playbackState) {
                    Player.STATE_READY -> {
                        binding!!.videoProgress.visibility = View.GONE
                      //  binding!!.ibwatchMovie.visibility = View.GONE
                        binding!!.rloptions.visibility = View.VISIBLE

                    }
                    Player.STATE_ENDED -> {
                        binding!!.ibwatchMovie.visibility = View.VISIBLE
                    }

                    Player.STATE_BUFFERING -> {
                        binding!!.videoProgress.visibility = View.VISIBLE

                    }
                    Player.STATE_IDLE -> {

                    }
                }
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                super.onPlayerError(error)
                Log.d("checkTime", error.toString())
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
                prepareMedia(Uri.parse(url), durationMillis!!)
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

    private fun singleDetailData() {
        try {
            var userId = ""
            if(mobileVerificationResult != null)
                userId = mobileVerificationResult!!._id
            val rootObject = SingleDetailRequest(id, userId)

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getSingleVideoDetail(rootObject)
            call.enqueue(object : Callback<SignleDetailResponse> {
                override fun onResponse(
                    call: Call<SignleDetailResponse>,
                    response: Response<SignleDetailResponse>
                ) {
                    val singleDetailModel = response.body()!!
                    if (singleDetailModel.code == 200) {
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            setUI(
                                singleDetailModel.result.detail,
                                singleDetailModel.result.common_videos
                            )
                            detailModels = singleDetailModel.result.detail
                            if (singleDetailModel.result.detail.current_session != null) {
                                val gson = Gson()
                                val json = preferenceStore!!.getUserDetail()
                                mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
                                if(preferenceStore!!.getUserDetail().isNullOrEmpty() ){
                                //    openSubscriptionDialog()
                                }else if(mobileVerificationResult != null && mobileVerificationResult!!.subscription == null)
                                  //  openSubscriptionDialog()
                                else {
                                    val onTime =
                                        singleDetailModel.result.detail.current_session.on_time
                                    if (!onTime.isNullOrEmpty())
                                        durationMillis = TimeUnit.SECONDS.toMillis(onTime.toLong())
                                }

                            }

                            for (model in singleDetailModel.result.detail.video) {
                                quality = model.main
                                quality[0].isSelected = true
                            }
                        } catch (ex: Exception) {

                        }


                    }


                }


                override fun onFailure(call: Call<SignleDetailResponse>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun singleSeriesData() {
        try {
            var userId = ""
            if(mobileVerificationResult != null)
                userId = mobileVerificationResult!!._id
            val rootObject = SingleDetailRequest(id, userId)

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getSeriesDetail(rootObject)
            call.enqueue(object : Callback<SeriesDetailModelResponse> {
                override fun onResponse(
                    call: Call<SeriesDetailModelResponse>,
                    response: Response<SeriesDetailModelResponse>
                ) {
                    val singleDetailModel = response.body()
                    seriesModel = singleDetailModel!!.result
                    if (singleDetailModel.code == 200) {
                        try {
                            binding!!.progressCircular.visibility = View.GONE
                            setUI(singleDetailModel.result)


                            if(sessionId == "" && seriesModel!!.season.size > 0 )
                                sessionId = seriesModel!!.season[0]._id
                            getSessionData(sessionId)
                        } catch (ex: Exception) {

                        }


                    }


                }


                override fun onFailure(call: Call<SeriesDetailModelResponse>, t: Throwable) {
                    binding!!.progressCircular.visibility = View.GONE
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

     fun getSessionData(sessionId: String) {
        try {
            var userId = ""
            if(mobileVerificationResult != null)
                userId = mobileVerificationResult!!._id
            val rootObject = SessionDetailRequest(sessionId, id, userId)

            val apiService = service!!.getClient().create(ApiInterface::class.java)
            val call = apiService.getSessionDetail(rootObject)
            call.enqueue(object : Callback<SessionDetailResponse> {
                override fun onResponse(
                    call: Call<SessionDetailResponse>,
                    response: Response<SessionDetailResponse>
                ) {
                    val sessionModel = response.body()!!
                    if (sessionModel.code == 200) {
                        try {
                            episodeAdapter!!.setEpisodeAdapter(
                                this@DetailActivity,
                                sessionModel.result
                            )

                            for(model in sessionModel.result){
                                if(videoModel != null && videoModel?.episode_id== model.episode._id){
                                    val gson = Gson()
                                    val json = preferenceStore!!.getUserDetail()
                                    mobileVerificationResult = gson.fromJson(json, MobileVerificationResult::class.java)
                                    if(preferenceStore!!.getUserDetail().isNullOrEmpty() ){
                                      //  openSubscriptionDialog()
                                    }else if(mobileVerificationResult != null && mobileVerificationResult!!.subscription == null)
                                        //openSubscriptionDialog()
                                    else{
                                        this@DetailActivity.episodeModel = model
                                        binding!!.ibwatchMovie.visibility = View.GONE
                                        binding!!.videoPlayer.visibility = View.VISIBLE
                                        binding!!.videoProgress.visibility = View.VISIBLE
                                        binding!!.image.animate()
                                            .alpha(100f)
                                            .setDuration(500)
                                            .setListener(object : AnimatorListenerAdapter() {
                                                override fun onAnimationEnd(animation: Animator?) {
                                                    binding!!.image.visibility = View.GONE
                                                }
                                            })
                                        isSeries = true
                                        episodeid = model.episode._id
                                        language_id = model.episode.video[0].language._id
                                        url = model.episode.video[0].main[0].url
                                        val  onTime = model.current_session?.on_time
                                        if (!onTime.isNullOrEmpty())
                                            durationMillis = TimeUnit.SECONDS.toMillis(onTime.toLong())
                                        prepareMedia(Uri.parse(url), durationMillis!!)
                                        for (models in model.episode.video) {
                                            quality = models.main
                                            quality[0].isSelected = true
                                        }
                                        updateDescriptionData()
                                    }
                                    }

                            }

                        } catch (ex: Exception) {

                        }


                    }


                }


                override fun onFailure(call: Call<SessionDetailResponse>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun setUI(detailModel: SeriesListModels?){
        if(detailModel != null){
                binding!!.latest.visibility = View.GONE
            binding!!.ibwatchMovie.visibility = View.GONE
                val layoutManager  = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                binding!!.verticalRecycler.layoutManager = layoutManager
                episodeAdapter = EpisodeAdapter()
                binding!!.verticalRecycler.adapter = episodeAdapter
               episodeAdapter!!.setListner(object : EpisodeAdapter.OnListener {
                   override fun click(episodeModel: SessionDetail) {
                       val gson = Gson()
                       val json = preferenceStore!!.getUserDetail()
                       mobileVerificationResult =
                           gson.fromJson(json, MobileVerificationResult::class.java)
                       if (preferenceStore!!.getUserDetail().isNullOrEmpty()) {
                           openSubscriptionDialog()
                       } else if (mobileVerificationResult != null && mobileVerificationResult!!.subscription == null)
                           openSubscriptionDialog()
                       else {
                           binding!!.scrollView.smoothScrollTo(0, binding!!.rlVideoPlay.top)
                           this@DetailActivity.episodeModel = episodeModel
                           binding!!.ibwatchMovie.visibility = View.VISIBLE
                           binding!!.videoPlayer.visibility = View.GONE
                           binding!!.videoProgress.visibility = View.GONE
                           binding!!.image.visibility = View.VISIBLE

                           pauseVideo()
                           isSeries = true
                           episodeid = episodeModel.episode._id
                           language_id = episodeModel.episode.video[0].language._id
                           url = episodeModel.episode.video[0].main[0].url
                           durationMillis = 0

                           for (model in episodeModel.episode.video) {
                               quality = model.main
                               quality[0].isSelected = true
                           }
                       }

                       updateDescriptionData()
                   }


               })
            ShowingImage.showImage(this, detailModel.cover, binding!!.image)
            binding!!.tvname.text = detailModel.name
            binding!!.tvdirector.text = detailModel.director
            binding!!.tvstarring.text = detailModel.starring
            binding!!.tvdescription.text = detailModel.description

            binding!!.seasonSpinner.visibility =View.VISIBLE
            val  seasondapter =  SeasonSpinnerAdapter(this@DetailActivity, detailModel.season)
            binding!!.seasonSpinner.adapter = seasondapter

            binding!!.seasonSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val seasonId = detailModel.season[position]._id

                   getSessionData(seasonId)


                }

            }

            binding!!.tvwatch.setOnClickListener {
                isMovieUrl = false

                if(!detailModel.trailer.isNullOrEmpty()){
                    binding!!.videoPlayer.visibility = View.VISIBLE
                    binding!!.image.animate()
                        .alpha(100f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                binding!!.image.visibility = View.GONE
                            }
                        })
                    url = detailModel.trailer
                    prepareMedia(Uri.parse(detailModel.trailer), 0)

                }else{
                    Toast.makeText(this, "Trailer Not Available", Toast.LENGTH_SHORT).show()
                }
            }

            binding!!.ibwatchMovie.setOnClickListener {
                binding!!.videoPlayer.visibility = View.VISIBLE
                binding!!.videoProgress.visibility = View.VISIBLE
                binding!!.ibwatchMovie.visibility = View.GONE
                binding!!.image.animate()
                    .alpha(100f)
                    .setDuration(500)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            binding!!.image.visibility = View.GONE
                        }
                    })
                durationMillis = 0
                prepareMedia(Uri.parse(url), durationMillis!!)
            }
            binding!!.btWatch.visibility = View.GONE


            binding!!.btFull.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            binding!!.ibExit.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            binding!!.quality.setOnClickListener {
                if(episodeModel != null){
                    openDialog(episodeModel!!.episode.video, quality,false)
                }

            }

            binding!!.Fullquality.setOnClickListener {
                if(episodeModel != null){
                    openDialog(episodeModel!!.episode.video, quality,false)
                }

            }

            binding!!.language.setOnClickListener {
               // openDialog(detailModel.video, null)
            }

            binding!!.share.setOnClickListener {
                onShareClick()
            }

            binding!!.ibCast.setOnClickListener {
                openCastDialog()
            }

            binding!!.FullibCast.setOnClickListener {
                openCastDialog()
            }

        }

        binding!!.rlVideoPlay.setOnClickListener {
            binding!!.rloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rloptions.visibility = View.GONE
            }, 5000)
        }

        binding!!.fullvideoPlayer.setOnClickListener {
            binding!!.rlFulloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rlFulloptions.visibility = View.GONE
            }, 5000)
        }

        binding!!.videoPlayer.setOnClickListener {
            binding!!.rloptions.visibility = View.VISIBLE
            Handler().postDelayed({
                binding!!.rloptions.visibility = View.GONE
            }, 5000)
        }


        Handler().postDelayed({
            binding!!.rloptions.visibility = View.GONE
            binding!!.rlFulloptions.visibility = View.GONE
        }, 5000)


    }

    private fun updateProgressBar() {
        durationMillis = if (simpleExoPlayer == null) 0 else simpleExoPlayer!!.currentPosition

       /* if (!dragging) {
            mSeekBar.setProgress(progressBarValue(position))
        }*/
        val bufferedPosition: Long =
            if (simpleExoPlayer == null) 0 else simpleExoPlayer!!.bufferedPosition
     //   mSeekBar.setSecondaryProgress(progressBarValue(bufferedPosition))

        // Remove scheduled updates.
        handler.removeCallbacks(updateProgressAction)
        // Schedule an update if necessary.
        val playbackState =
            if (simpleExoPlayer == null) Player.STATE_IDLE else simpleExoPlayer!!.playbackState
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            var delayMs: Long
            if (simpleExoPlayer!!.playWhenReady && playbackState == Player.STATE_READY) {
                delayMs = 1000 - durationMillis!! % 1000
                if (delayMs < 200) {
                    delayMs += 1000
                }
            } else {
                delayMs = 1000
            }
            if(isFirstTime ){
                if(durationMillis!!  > simpleExoPlayer!!.duration -   50000){
                    videoFinish()
                    Log.d("bufferdPosition", simpleExoPlayer!!.duration.toString())

                    isFirstTime = false
                }
            }

            handler.postDelayed(updateProgressAction, delayMs)
        }
    }


    private val updateProgressAction = Runnable {
        updateProgressBar()
    }

    fun openDialog(list: ArrayList<VideoCategoryListModel>, qualityList: ArrayList<TrailerModel>?, isFromMoview:Boolean){
        val myDialog = LanguageDialog.newInstance(this, list, qualityList)
        myDialog.setListner(object : LanguageDialog.OnListener {
            override fun accept(quality: String) {
                selectedQuality = quality
                if (detailModels != null) {
                    for (model in detailModels!!.video) {
                        for (modelVideo in model.main) {
                            if (selectedQuality == modelVideo.type) {
                                if (!modelVideo.url.isNullOrEmpty()) {
                                    if(isFromMoview)
                                    isMovieUrl = true
                                    else
                                        isSeries = true
                                    binding!!.ibwatchMovie.visibility = View.GONE
                                    binding!!.videoPlayer.visibility = View.VISIBLE
                                    binding!!.videoProgress.visibility = View.VISIBLE
                                    binding!!.image.animate()
                                        .alpha(100f)
                                        .setDuration(500)
                                        .setListener(object : AnimatorListenerAdapter() {
                                            override fun onAnimationEnd(animation: Animator?) {
                                                binding!!.image.visibility = View.GONE
                                            }
                                        })
                                    url = modelVideo.url
                                    prepareMedia(Uri.parse(modelVideo.url), durationMillis!!)
                                    myDialog.dismiss()
                                }

                            }
                        }

                    }
                }


            }


        })
        myDialog.show(supportFragmentManager, "Hello Testing")
    }

    fun onShareClick(){
        val msg = getString(R.string.share_msg)
        val mInvitationUrl = "https://play.google.com/store/apps/details?id=com.isoftinc.films"
        val shareMessage = "\n" + msg + "\n" + mInvitationUrl + "\n"
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(intent, "choose one"))
    }



    private fun updateSession(isSeries: Boolean) {
        try {

            var userId = ""
            if(mobileVerificationResult != null)
                userId = mobileVerificationResult!!._id

            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(durationMillis!!)
            var rootObject : UpdateSessionRequest? = null
            if(isSeries){
             rootObject = UpdateSessionRequest(
                    userId,
                    episodeid,
                    seconds.toString(),
                 language_id
                 ,id
                )
            }else{
               rootObject = UpdateSessionRequest(
                    userId,
                    id,
                    seconds.toString(),
                    detailModels!!.video[0]._id,id
                )
            }

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.update(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(
                    call: Call<SuccessModel>,
                    response: Response<SuccessModel>
                ) {
                    val singleDetailModel = response.body()!!
                    if (singleDetailModel.code == 200) {

                    }


                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun videoFinish(){
        try {

            var userId = ""
            if(mobileVerificationResult != null)
                userId = mobileVerificationResult!!._id
            var rootObject : VideoFinishModel? = null
            val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(durationMillis!!)
            if(isMovies!!){
                rootObject = VideoFinishModel(userId, id)

            }else if (isSeries){
               rootObject = VideoFinishModel(userId, episodeid)
            }

            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.videoFinish(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(
                    call: Call<SuccessModel>,
                    response: Response<SuccessModel>
                ) {
                    val singleDetailModel = response.body()!!
                    if (singleDetailModel.code == 200) {

                    }


                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun openSubscriptionDialog(){
        val subscriptionBottomSheetDialog = SubscriptionBottomSheetDialog.newInstance(this, "")
        subscriptionBottomSheetDialog.setListner(object : SubscriptionBottomSheetDialog.OnListener {
            override fun accept(price: String, planId: String) {
                subscriptionBottomSheetDialog.dismiss()
                subId = planId
                startPayment(price)
            }

            override fun navigate() {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
              //  replaceFragment(RegisterFragment.newInstance(true))
            }

        })
        subscriptionBottomSheetDialog.show(
            supportFragmentManager,
            SubscriptionBottomSheetDialog().TAG
        )
    }

    private fun startPayment(price: String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */

        val co = Checkout()
        val activity: Activity = this
        val lastIndex = price.lastIndexOf(".")
        var subPrice = price.substring(0, lastIndex)
        subPrice = subPrice + "00"

        try {
            val options = JSONObject()
            options.put("name", "Filim")
            options.put("description", "Subscription")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", subPrice)

            val prefill = JSONObject()
            prefill.put("email", "test@razorpay.com")
            prefill.put("contact", "9876543210")

            options.put("prefill", prefill)
            co.open(activity, options)
        }catch (e: java.lang.Exception){
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentError(errorCode: Int, response: String?) {
        try{
            Toast.makeText(this, "Payment failed $errorCode \n $response", Toast.LENGTH_LONG).show()
        }catch (e: java.lang.Exception){
            Log.e("checkkk", "Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try{
            purchasePlan(razorpayPaymentId!!)
            Toast.makeText(this, "Payment Successful $razorpayPaymentId", Toast.LENGTH_LONG).show()
        }catch (e: java.lang.Exception){
            Log.e("checkkk", "Exception in onPaymentSuccess", e)
        }
    }


    private fun purchasePlan(razorpayPaymentId: String) {
        try {
            binding!!.progressCircular.visibility = View.VISIBLE
            val rootObject = PurchaseModelRequest(
                mobileVerificationResult!!._id,
                subId!!,
                razorpayPaymentId
            )
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.purchasePlan(rootObject)
            call.enqueue(object : Callback<SuccessModel> {
                override fun onResponse(
                    call: Call<SuccessModel>,
                    response: Response<SuccessModel>
                ) {
                    val singleDetailModel = response.body()!!
                    if (singleDetailModel.code == 200) {
                        try {
                            userGet()
                            Toast.makeText(this@DetailActivity, "Success", Toast.LENGTH_SHORT)
                                .show()

                        } catch (ex: Exception) {

                        }


                    }


                }


                override fun onFailure(call: Call<SuccessModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun userGet() {
        try {

            val rootObject = UserGetModel(mobileVerificationResult!!._id)
            val apiService = ApiClient().getClient().create(ApiInterface::class.java)
            val call = apiService.userget(rootObject)
            call.enqueue(object : Callback<MobileVerificationModel> {
                override fun onResponse(
                    call: Call<MobileVerificationModel>,
                    response: Response<MobileVerificationModel>
                ) {
                    val mobileVerificationModel = response.body()!!
                    if (mobileVerificationModel.code == 200) {
                        try {
                            val gson = Gson()
                            val json = gson.toJson(mobileVerificationModel.result)
                            preferenceStore!!.setUserDetail(json)
                            binding!!.progressCircular.visibility = View.GONE
                        } catch (ex: Exception) {

                        }


                    }


                }


                override fun onFailure(call: Call<MobileVerificationModel>, t: Throwable) {
                    Log.e("responseError", t.message)
                }


            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun openCastDialog(){
        val castingDialog = CastingDialog.newInstance(this)
        castingDialog.show(supportFragmentManager, "CastingDialog")
    }


    class checkInternetConnection(val context: Context, val llDetail: LinearLayout) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean? {
            return  AndroidUtilities().hasActiveInternetConnection(context)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if(!result!!){
                val  snackbar = Snackbar.make(
                    llDetail,
                    "Internet Connection not Available",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }

        }
    }

    fun updateDescriptionData(){
        try {
            binding!!.tvname.text = episodeModel!!.episode.name
          //  binding!!.tvdirector.text = episodeModel!!.episode.director
           // binding!!.tvstarring.text = episodeModel!!.episode.starring
            binding!!.tvdescription.text = episodeModel!!.episode.description
        }catch (ex: Exception){

        }

    }

}