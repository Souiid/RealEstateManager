package com.famoco.projet9

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Aspect_ratio: ImageVector
	get() {
		if (_Aspect_ratio != null) {
			return _Aspect_ratio!!
		}
		_Aspect_ratio = ImageVector.Builder(
            name = "Aspect_ratio",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(560f, 680f)
				horizontalLineToRelative(200f)
				verticalLineToRelative(-200f)
				horizontalLineToRelative(-80f)
				verticalLineToRelative(120f)
				horizontalLineTo(560f)
				close()
				moveTo(200f, 480f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(-120f)
				horizontalLineToRelative(120f)
				verticalLineToRelative(-80f)
				horizontalLineTo(200f)
				close()
				moveToRelative(-40f, 320f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(80f, 720f)
				verticalLineToRelative(-480f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(160f, 160f)
				horizontalLineToRelative(640f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(880f, 240f)
				verticalLineToRelative(480f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(800f, 800f)
				close()
				moveToRelative(0f, -80f)
				horizontalLineToRelative(640f)
				verticalLineToRelative(-480f)
				horizontalLineTo(160f)
				close()
				moveToRelative(0f, 0f)
				verticalLineToRelative(-480f)
				close()
			}
		}.build()
		return _Aspect_ratio!!
	}

private var _Aspect_ratio: ImageVector? = null
