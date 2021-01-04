package com.isoftinc.film.retrofit



import com.isoftinc.film.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // User SignUp
    @POST("user/sendOTPForAuthentication")
    fun userSignUp(@Body body: SignupRequestModel): Call<SuccessModel>


    @POST("user/socialLogin")
    fun socialLogin(@Body body: SocialModelRequest) : Call<MobileVerificationModel>

    @POST("user/verifyMobileNo")
    fun verifyMobileNo(@Body body: VerifyMobileModel) : Call<MobileVerificationModel>

    @POST("video/home")
    fun getHomeData(@Body body: HomeRequestModel) : Call<HomeModel>

    @POST("video/sub-category/list")
    fun getViewAll(@Body body: ViewAllRequest):Call<ViewAllResponse>

    @POST("video/detail")
    fun getSingleVideoDetail(@Body body: SingleDetailRequest):Call<SignleDetailResponse>

    @POST("plan/list")
    fun getPlanList():Call<PlanListModel>

    @POST("user/videoSession")
    fun update(@Body body: UpdateSessionRequest) : Call<SuccessModel>

    @POST("video/search")
    fun search(@Body body: SearchRequestModel) : Call<SearchModel>

    @POST("user/watchHistory")
    fun watchHistory(@Body body: WatchHistoryModel) : Call<WatchListModel>

    @POST("user/purchagePlan")
    fun purchasePlan(@Body body: PurchaseModelRequest) : Call<SuccessModel>

    @POST("user/get")
    fun userget(@Body body: UserGetModel) : Call<MobileVerificationModel>

    @POST("user/deleteHistory")
    fun deleteHistory(@Body body: UserGetModel) : Call<SuccessModel>

    @POST("user/editProfile")
    fun editProfile(@Body body: EditProfileRequestModel) :Call<MobileVerificationModel>

   @POST("video/continueWatch")
   fun continueWatching(@Body body: UserGetModel) : Call<ContinueResponseModel>

    @POST("video/latest")
    fun getLatest(@Body body: UserGetModel) : Call<ContinueResponseModel>

    @POST("video/trending")
    fun getTrending(@Body body: UserGetModel) : Call<ContinueResponseModel>

    @POST("video/movielist")
    fun getMovieList(@Body body: HomeRequestModel) : Call<HomeModel>

    @POST("video/finish")
    fun videoFinish(@Body body: VideoFinishModel?) : Call<SuccessModel>

    @POST("series/list")
    fun getSeriesList(@Body body: SeriesRequestModel) : Call<SeriesModelListResponse>

    @POST("series/list")
    fun getSeriesHomeList(@Body body: SeriesHomeRequestModel) : Call<SeriesModelListResponse>

    @POST("series/detail")
    fun getSeriesDetail(@Body body: SingleDetailRequest) : Call<SeriesDetailModelResponse>

    @POST("series/seasonDetail")
    fun getSessionDetail(@Body body: SessionDetailRequest) : Call<SessionDetailResponse>

    @POST("user/feedback")
    fun feedback(@Body body: feedbackRequestModel) : Call<SuccessModel>


}