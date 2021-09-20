package com.abumadi.customview.Views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.abumadi.customview.R
import kotlin.math.pow


class CustomView(
    context: Context, attrs: AttributeSet
) : View(context, attrs) {

    private val rect = Rect()
    private val squarePaint = Paint()
    private val circlePaint = Paint()
    private val linePaint =Paint()

    //square
    private var mSquareColor: Int? = null
    private var mSquareSize: Int? = null

    //circle
    private var mCircleX: Float? = null
    private var mCircleY: Float? = null
    private var mCircleRadius: Float = 100f

    //image
    private var mImage: Bitmap? = null


    private companion object {
        const val SQUARE_SIZE_DEF = 500
    }

    init {
        squarePaint.isAntiAlias//blur for pixels of lines
        setupAttributes(attrs)
        circlePaint.color = Color.parseColor("#00ccff")
        linePaint.color=Color.BLUE
        mImage = BitmapFactory.decodeResource(resources, R.drawable.backgroun)

        //image appear with its normal dimens
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                //Resize image
                mImage = getResizedBitmap(mImage, width, height)
            }
        })
    }
    fun getResizedBitmap(bm: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm?.width
        val height = bm?.height
        val scaleWidth = newWidth.toFloat() / width!!
        val scaleHeight = newHeight.toFloat() / height!!
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()//for memory leaks
        return resizedBitmap
    }

    private fun setupAttributes(attrs: AttributeSet?) {

        val typedArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomView,
            0, 0
        )

        mSquareColor = typedArray.getColor(R.styleable.CustomView_square_color, Color.GREEN)//(attrs shape,default)
        mSquareSize =
            typedArray.getDimensionPixelSize(R.styleable.CustomView_square_size, SQUARE_SIZE_DEF)

        squarePaint.color = mSquareColor!!
        typedArray.recycle()//must be in the last line of code
    }

    fun swapColor() {
        if (squarePaint.color == mSquareColor)
            squarePaint.color = Color.RED
        else {
            squarePaint.color = mSquareColor!!
        }
        postInvalidate()//call onDraw method again->async
//        invalidate() //sync
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(mImage!!, 0f, 0f, null)

        rect.left = 50
        rect.top = 50
        rect.right = rect.left + mSquareSize!!
        rect.bottom = rect.top + mSquareSize!!

        canvas?.drawRect(rect, squarePaint)

        if (mCircleX == null || mCircleY == null || mCircleX == 0f || mCircleY == 0f) {
            mCircleX = (width / 2).toFloat()
            mCircleY = (height / 2).toFloat()

        }
        canvas?.drawCircle(mCircleX!!, mCircleY!!, mCircleRadius, circlePaint)
        val startX = 0.0f
        val startY = 0.0f
        val stopX = 600f
        val stopY = 600f


        canvas?.drawLine(startX,startY,stopX,stopY,linePaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value: Boolean = super.onTouchEvent(event)

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {//increase circle radius with rect click
                val x: Float = event.x
                val y: Float = event.y
                if (rect.left < x && rect.right > x)
                    if (rect.top < y && rect.bottom > y) {
                        mCircleRadius += 10f
                        postInvalidate()
                    }
                return true
            }
            MotionEvent.ACTION_MOVE -> {//moving circle inside custom view
                val x: Float = event.x
                val y: Float = event.y

                val dx: Float = (x - mCircleX!!).pow(2)
                val dy: Float = (y - mCircleY!!).pow(2)

                if (dx + dy < (mCircleRadius).pow(2)) {
                    //touched
                    mCircleX = x
                    mCircleY = y
                    postInvalidate()

                    return true
                }
                return value
            }
        }
        return value
    }
}
