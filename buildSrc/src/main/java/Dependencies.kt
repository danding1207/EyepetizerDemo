
object Versions {
    const val room = "1.1.0"
    const val lifecycle = "1.1.1"
    const val support = "27.1.1"
    const val retrofit = "2.3.0"
    const val gson = "2.8.5"
    const val okhttp = "3.4.1"
    const val okhttp_logging_interceptor = "3.9.0"
    const val mockwebserver = "3.8.1"
    const val arouter_version = "1.3.1"
    const val arouter_processor_version = "1.1.4"
    const val constraint_layout = "1.0.2"
    const val glide = "4.7.1"
    const val android_gradle_plugin = "3.1.2"
    const val rxjava = "2.1.9"
    const val rx_android = "2.0.1"
    const val logger = "2.2.0"
    const val kotlin = "1.2.41"
    const val paging = "1.0.0"
    const val work = "1.0.0-alpha02"
    const val navigation = "1.0.0-alpha01"
    const val bottomnavigationviewex = "1.2.4"
    const val smartrefreshlayout = "1.1.0-alpha-7"
    const val inflationx_calligraphy3 = "3.0.0"
    const val inflationx_viewpump = "1.0.0"
    const val tangram = "2.0.5"
    const val exoplayer = "2.8.1"
    const val photoView = "2.1.3"

}

object Libs {

    /** support */
    val support_annotations = "com.android.support:support-annotations:${Versions.support}"
    val support_app_compat = "com.android.support:appcompat-v7:${Versions.support}"
    val support_recyclerview = "com.android.support:recyclerview-v7:${Versions.support}"
    val support_cardview = "com.android.support:cardview-v7:${Versions.support}"
    val support_design = "com.android.support:design:${Versions.support}"
    val support_v4 = "com.android.support:support-v4:${Versions.support}"
    val support_core_utils = "com.android.support:support-core-utils:${Versions.support}"
    val support_constraint_layout = "com.android.support.constraint:constraint-layout:${Versions.constraint_layout}"

    /** room 数据库 */
    val room_runtime = "android.arch.persistence.room:runtime:${Versions.room}"
    val room_compiler = "android.arch.persistence.room:compiler:${Versions.room}"
    val room_rxjava2 = "android.arch.persistence.room:rxjava2:${Versions.room}"
    val room_testing = "android.arch.persistence.room:testing:${Versions.room}"

    /** lifecycle 生命周期 */
    val lifecycle_runtime = "android.arch.lifecycle:runtime:${Versions.lifecycle}"
    val lifecycle_extensions = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    val lifecycle_java8 = "android.arch.lifecycle:common-java8:${Versions.lifecycle}"
    val lifecycle_compiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"

    /** paging 分页加载 */
    val paging_runtime = "android.arch.paging:runtime:${Versions.paging}"

    /** work 后台服务 */
    val work_runtime = "android.arch.work:work-runtime:${Versions.work}"
    val work_testing = "android.arch.work:work-testing:${Versions.work}"
    val work_firebase = "android.arch.work:work-firebase:${Versions.work}"
    val work_runtime_ktx = "android.arch.work:work-runtime-ktx:${Versions.work}"

    /** navigation 导航 */
    val navigation_runtime = "android.arch.navigation:navigation-runtime:${Versions.navigation}"
    val navigation_runtime_ktx = "android.arch.navigation:navigation-runtime-ktx:${Versions.navigation}"
    val navigation_fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    val navigation_fragment_ktx = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val navigation_safe_args_plugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    val navigation_testing_ktx = "android.arch.navigation:navigation-testing-ktx:${Versions.navigation}"


    /** bottomnavigationviewex 底部菜单 */
    val bottomnavigationviewex_runtime = "com.github.ittianyu:BottomNavigationViewEx:${Versions.bottomnavigationviewex}"

    /** arouter 路由 */
    val arouter_runtime = "com.alibaba:arouter-api:${Versions.arouter_version}"
    val arouter_compiler = "com.alibaba:arouter-compiler:${Versions.arouter_processor_version}"

    /** retrofit 网络请求 */
    val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val retrofit_mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"

    /** okhttp 网络请求 */
    val okhttp_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp_logging_interceptor}"
    val okhttp_mock_web_server = "com.squareup.okhttp3:mockwebserver:${Versions.mockwebserver}"
    val okhttp_runtime = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"

    /** gson json解析 */
    val gson_runtime  = "com.google.code.gson:gson:${Versions.gson}"

    /** kotlin  */
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val kotlin_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val kotlin_allopen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"

    /** glide 图片加载 */
    val glide_runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    /** rxjava */
    val rxjava_rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    val rxjava_rx_android = "io.reactivex.rxjava2:rxandroid:${Versions.rx_android}"

    /** smartrefreshlayout */
    val smartrefreshlayout = "com.scwang.smartrefresh:SmartRefreshLayout:${Versions.smartrefreshlayout}"
    val smartRefreshHeader = "com.scwang.smartrefresh:SmartRefreshHeader:${Versions.smartrefreshlayout}"

    /** logger */
    val logger_runtime  = "com.orhanobut:logger:${Versions.logger}"

    /** inflationx 自定义字体 */
    val inflationx_calligraphy3  = "io.github.inflationx:calligraphy3:${Versions.inflationx_calligraphy3}"
    val inflationx_viewpump  = "io.github.inflationx:viewpump:${Versions.inflationx_viewpump}"

    /** tangram 动态布局 */
    val tangram_runtime  = "com.alibaba.android:tangram:${Versions.tangram}"

    /** exoplayer 视频播放 */
    val exoplayer_runtime  = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
    val exoplayer_core  = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"// exoplayer-core：核心功能（必需）。
    val exoplayer_dash  = "com.google.android.exoplayer:exoplayer-dash:${Versions.exoplayer}"// exoplayer-dash：支持DASH内容。
    val exoplayer_hls  = "com.google.android.exoplayer:exoplayer-hls:${Versions.exoplayer}"// exoplayer-hls：支持HLS内容。
    val exoplayer_smoothstreaming  = "com.google.android.exoplayer:exoplayer-smoothstreaming:${Versions.exoplayer}"// exoplayer-smoothstreaming：支持SmoothStreaming内容。
    val exoplayer_ui  = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"// exoplayer-ui：用于ExoPlayer的UI组件和资源。

    /** photoView 视频播放 */
    val photoView_runtime  = "com.github.chrisbanes:PhotoView:${Versions.photoView}"

    /** android_gradle_plugin */
    val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"

    /** build_versions */
    val build_versions_compile_sdk = 27
    val build_versions_min_sdk = 21
    val build_versions_target_sdk = 27
    val build_versions_build_tools = "27.0.3"

    /** versionCode */
    val versionCode = 1
    val versionName = "1.0.0"
    val applicationId = "com.msc.eyepetizer"


}