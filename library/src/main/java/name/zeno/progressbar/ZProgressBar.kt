package name.zeno.progressbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.Gravity
import android.view.View

/**
 * 1. 绘制背景
 * 2. 绘制 process
 * 3. 绘制左边文字
 * 4. 绘制右边文字
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/13
 */
class ZProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
  var process: Float = 0.5F
    set(value) {
      if (value < 0 || value > 1) throw IllegalArgumentException("process 请设置 0 ~ 1的浮点数")
      field = value
      initRect(measuredWidth, measuredWidth)
      shader = LinearGradient(0F, 0F, measuredWidth * process, 0F, colorStart, colorEnd, Shader.TileMode.CLAMP)
      invalidate()// 重绘
    }

  private var radius: Float = 16F
  @ColorInt private var colorBg: Int = Color.LTGRAY
  @ColorInt private var colorStart: Int = Color.BLUE
  @ColorInt private var colorEnd: Int = Color.GREEN

  var text: String = "50/100"
    set(value) {
      field = value
      invalidate()
    }
  var textSize: Float = 16F
  var gravity: Int = Gravity.CENTER
  @ColorInt private var colorTextLeft: Int = Color.WHITE
  @ColorInt private var colorTextRight: Int = Color.DKGRAY

  // 绘制工具
  private val textPaint = Paint()
  private val imgPaint = Paint()
  private lateinit var shader: Shader
  private var lBmp: Bitmap? = null
  private var rBmp: Bitmap? = null
  private var lCanvas: Canvas? = null
  private var rCanvas: Canvas? = null

  // 绘制范围辅助
  private val fullRect = RectF()
  private val leftRect = RectF()
  private val rightRect = RectF()

  init {
    imgPaint.isAntiAlias = true
    imgPaint.color = colorBg
    imgPaint.style = Paint.Style.FILL

    textPaint.isAntiAlias = true
    textPaint.textSize = textSize

    initAttrs(context, attrs, defStyleAttr)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    initCanvas(w, h)
    initRect(w, h)
    shader = LinearGradient(0F, 0F, measuredWidth * process, 0F, colorStart, colorEnd, Shader.TileMode.CLAMP)
  }


  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    // 绘制背景
    imgPaint.shader = null
    canvas.drawRoundRect(fullRect, radius, radius, imgPaint)

    // 绘制进度
    imgPaint.shader = shader
    canvas.drawRoundRect(leftRect, radius, radius, imgPaint)


    // 绘制文字
    drawText(canvas, false)
    drawText(canvas, true)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    lBmp?.recycle()
    rBmp?.recycle()
  }

  private fun initCanvas(w: Int, h: Int) {
    lBmp?.recycle()
    rBmp?.recycle()
    lBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    rBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    lCanvas = Canvas(lBmp)
    rCanvas = Canvas(rBmp)
  }

  private fun initRect(w: Int, h: Int) {
    fullRect.set(0F, 0F, w.toFloat(), h.toFloat())
    leftRect.set(0F, 0F, w * process, h.toFloat())
    rightRect.set(w * process, 0F, w.toFloat(), h.toFloat())
  }

  @SuppressLint("RtlHardcoded")
  private fun drawText(target: Canvas, right: Boolean = false) {
    val canvas: Canvas?
    val rect: RectF?
    val bmp: Bitmap
    if (right) {
      canvas = rCanvas ?: return
      bmp = rBmp ?: return
      rect = rightRect
      textPaint.color = colorTextRight
    } else {
      canvas = lCanvas ?: return
      bmp = lBmp ?: return
      rect = leftRect
      textPaint.color = colorTextLeft
    }

    canvas.save()
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) // 清楚画板内容

    textPaint.textSize = textSize
    val fm = textPaint.fontMetrics

    val textW = textPaint.measureText(text)

    val x = when (gravity) {
      Gravity.LEFT -> paddingLeft.toFloat()
      Gravity.RIGHT -> measuredWidth - textW - paddingRight
      else -> (measuredWidth - paddingLeft - paddingRight - textW) / 2 + paddingLeft
    }

    val y = measuredHeight / 2 - fm.descent + (fm.bottom - fm.top) / 2

    canvas.clipRect(rect)
    canvas.drawText(text, x, y, textPaint)
    target.drawBitmap(bmp, 0F, 0F, imgPaint)

    canvas.restore()
  }

  private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.ZProgressBar, defStyleAttr, 0)
    gravity = ta.getInt(R.styleable.ZProgressBar_android_gravity, gravity)
    textSize = ta.getDimension(R.styleable.ZProgressBar_android_textSize, textSize)
    colorTextLeft = ta.getColor(R.styleable.ZProgressBar_pb_colorTextLeft, colorTextLeft)
    colorTextRight = ta.getColor(R.styleable.ZProgressBar_pb_colorTextRight, colorTextRight)
    radius = ta.getDimension(R.styleable.ZProgressBar_pb_radius, radius)
    colorBg = ta.getColor(R.styleable.ZProgressBar_pb_colorBg, colorBg)
    colorStart = ta.getColor(R.styleable.ZProgressBar_pb_colorStart, colorStart)
    colorEnd = ta.getColor(R.styleable.ZProgressBar_pb_colorEnd, colorEnd)
    if (ta.hasValue(R.styleable.ZProgressBar_android_text)) {
      text = ta.getText(R.styleable.ZProgressBar_android_text).toString()
    }
    process = ta.getFloat(R.styleable.ZProgressBar_pb_process, process)
    ta.recycle()
  }

}