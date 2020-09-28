package com.yannis.baselib.utils.glidex

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
class GlideRectFTransform(
    private var context: Context,
    private var radiusDP: Int,
    private var frameDp: Int,
    private var color: Int
) :
    BitmapTransformation() {

    private val ID: String = "com.bumptech.glide.transformations.GlideRoundTransform"
    private val ID_BYTES: ByteArray = ID.toByteArray(Charset.forName("UTF-8"))
    private var radius: Float = 0f
    private var frame: Float = 0f

    init {
        radius = Resources.getSystem().displayMetrics.density * radiusDP
        frame = Resources.getSystem().displayMetrics.density * frameDp
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        // 将图片按照比例放大到 imageView 的尺寸
        //val centerCrop = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return roundCrop(pool, toTransform)
    }

    private fun roundCrop(pool: BitmapPool, centerCrop: Bitmap?): Bitmap? {
        if (centerCrop == null) {
            return null
        }

        var bitmap = pool.get(centerCrop.width, centerCrop.height, Bitmap.Config.RGB_565)

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(centerCrop.width, centerCrop.height, Bitmap.Config.RGB_565)
        }

        // 绘制图片
        val canvas = Canvas(bitmap)
        // 设置背景色，否则为黑色
        canvas.drawColor(Color.WHITE)
        val paint = Paint()
        paint.shader = BitmapShader(centerCrop, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectFFrame = RectF(0f, 0f, centerCrop.width.toFloat(), centerCrop.width.toFloat())
        if (frame != -1f && color != -1) {
            val paintFrame = Paint()
            paintFrame.color = color
            paintFrame.isAntiAlias = true
            canvas.drawRoundRect(rectFFrame, radius + frame, radius + frame, paintFrame)
            val rectF = RectF(
                0f + frame,
                0f + frame,
                centerCrop.width.toFloat() - frame,
                centerCrop.width.toFloat() - frame
            )
            canvas.drawRoundRect(rectF, radius, radius, paint)
        } else {
            val rectF = RectF(0f, 0f, centerCrop.width.toFloat(), centerCrop.width.toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
        }

        return bitmap
    }

    /**
     * 下面三个方法需要实现，不然会出现刷新闪烁
     */
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val radiusData: ByteArray = ByteBuffer.allocate(4).putFloat(radius).array()
        messageDigest.update(radiusData)
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), Util.hashCode(radius))
    }

    override fun equals(other: Any?): Boolean {

        if (other is GlideRectFTransform) {
            val obj: GlideRectFTransform = other as GlideRectFTransform
            return radius == obj.radius
        }
        return false
    }
}