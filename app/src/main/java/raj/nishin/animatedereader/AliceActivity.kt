package raj.nishin.animatedereader

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import kotlinx.android.synthetic.main.content_alice.*
import kotlinx.android.synthetic.main.toolbox.*

class AliceActivity : AppCompatActivity() {

    private val cardHighElevation = 16f
    private val cardLowElevation = 0f
    private var elevationAnimator: ObjectAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alice)

        cvToolbox.cardElevation = cardHighElevation

        val coverImageHeight = resources.getDimension(R.dimen.cover_image_height)

        nsv.postDelayed({
            nsv.scrollTo(0, coverImageHeight.toInt())
        }, 10)

        nsv.postDelayed({
            ObjectAnimator.ofInt(nsv, "scrollY", 0).apply {
                duration = 1000
                start()
            }
        }, 2000)

        nsv.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == 0) {
                coverImage.translationY = 0f
            } else {
                coverImage.translationY = -scrollY / 3f
            }

            if (scrollY > coverImageHeight) {
                stickyChapter.visibility = View.VISIBLE
            } else {
                stickyChapter.visibility = View.GONE
            }

            val scrollThresholdToolbox = v.getChildAt(0).height - v.height - 10
            if (scrollY >= scrollThresholdToolbox && cvToolbox.cardElevation == cardHighElevation) {
                animateElevation(start = cardHighElevation, end = cardLowElevation)
            } else if (scrollY < scrollThresholdToolbox && cvToolbox.cardElevation == cardLowElevation) {
                animateElevation(start = cardLowElevation, end = cardHighElevation)
            }
        }
    }

    private fun animateElevation(start: Float, end: Float) {
        elevationAnimator?.cancel()
        elevationAnimator = ObjectAnimator.ofFloat(cvToolbox, "cardElevation", start, end).apply {
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}

