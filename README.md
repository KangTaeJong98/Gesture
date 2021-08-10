# GestureLayout, GestureListener

## GestureLayout
* View를 추가할 경우 터치 영역을 넓히기 위한 Padding과 GestureListener를 자동으로 View에 설정합니다.
* When you add View, automatically set Paddings and GestureListener to expand the touch area in View.

## GestureListener
* 인스타그램 스토리처럼 View를 드래그, 확대/축소, 회전을 할 수 있는 Listener입니다.
* It's a listener who can drag, zoom, and rotate View like an Instagram story.

<img src="./readme/0.png" alt="0" width="25%"><img src="./readme/1.png" alt="1" width="25%"><img src="./readme/2.png" alt="2" width="25%"><img src="./readme/3.png" alt="3" width="25%">

## ⚡ Features

### 설명 : https://rkdxowhd98.tistory.com/193
### Explain : https://rkdxowhd98.tistory.com/193

### GestureLayout
```kotlin
class GestureLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    var childViewPadding by Delegates.observable(0) { _, _, new ->
        for (view in children) {
            view.setPadding(new)
        }
    }

    var listener by Delegates.observable(GestureListener()) { _, _, new ->
        for (view in children) {
            view.setOnTouchListener(new)
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GestureLayout, defStyleAttr, defStyleRes).apply {
            childViewPadding = getDimensionPixelSize(R.styleable.GestureLayout_childViewPadding, 0)
        }
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        child.setPadding(childViewPadding)
        child.setOnTouchListener(listener)
    }
}
```

### GestureListener
* 드래그(Drag)
```kotlin
    open class GestureListener : View.OnTouchListener {
    private val point by lazy { PointF() }
    private val vector by lazy { Vector() }
    private var distance = 0F
    private var mode = Mode.NONE

    companion object {
        fun distance(event: MotionEvent): Float {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)

            return sqrt(x*x + y*y)
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(view, event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                onActionPointerDown(view, event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onActionPointerUp(view, event)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                view.performClick()
                onActionCancel(view, event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == Mode.DRAG && event.pointerCount == 1) {
                    onActionDrag(view, event)
                } else if (mode == Mode.SCALE && event.pointerCount == 2) {
                    onActionScale(view, event)
                }
            }
        }

        return true
    }

    open fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float) {
        val array = arrayOf(distanceX, distanceY).toFloatArray()

        view.matrix.mapVectors(array)
        view.translationX += array.first()
        view.translationY += array.last()
    }

    open fun onDragStart(view: View, event: MotionEvent) {

    }

    open fun onDragEnd(view: View, event: MotionEvent) {

    }

    open fun onScaleStart(view: View, event: MotionEvent) {

    }

    open fun onScale(view: View, event: MotionEvent, scale: Float) {
        view.scaleX *= scale
        view.scaleY *= scale
    }

    open fun onScaleEnd(view: View, event: MotionEvent) {

    }

    open fun onRotateStart(view: View, event: MotionEvent) {

    }

    open fun onRotate(view: View, event: MotionEvent, rotation: Float) {
        view.rotation = rotation
    }

    open fun onRotateEnd(view: View, event: MotionEvent) {

    }

    private fun onActionDrag(view: View, event: MotionEvent) {
        val distanceX = event.x - point.x
        val distanceY = event.y - point.y

        onDrag(view, event, distanceX, distanceY)
    }

    private fun onActionScale(view: View, event: MotionEvent) {
        onScale(view, event, distance(event) / distance)
        onRotate(view, event, view.rotation + Vector.getDegree(vector, Vector(event)))
    }

    private fun onActionDown(view: View, event: MotionEvent) {
        mode = Mode.DRAG
        onActionDragDown(view, event)
    }

    private fun onActionDragDown(view: View, event: MotionEvent) {
        point.set(event.x, event.y)
        onDragStart(view, event)
    }

    private fun onActionPointerDown(view: View, event: MotionEvent) {
        mode = Mode.SCALE
        onDragEnd(view, event)
        onActionScalePointerDown(view, event)
    }

    private fun onActionScalePointerDown(view: View, event: MotionEvent) {
        vector.set(event)
        distance = distance(event)
        onScaleStart(view, event)
        onRotateStart(view, event)
    }

    private fun onActionPointerUp(view: View, event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
                onDragEnd(view, event)
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                onActionDragPointerUp(view, event)
                onScaleEnd(view, event)
                onRotateEnd(view, event)
            }
            else -> {
                mode = Mode.NONE
            }
        }
    }

    private fun onActionDragPointerUp(view: View, event: MotionEvent) {
        val index = if (event.actionIndex == 0) 1 else 0
        point.set(event.getX(index), event.getY(index))
        onDragStart(view, event)
    }

    private fun onActionCancel(view: View, event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
                onDragEnd(view, event)
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                onScaleEnd(view, event)
                onRotateEnd(view, event)
            }
            else -> {
                mode = Mode.NONE
            }
        }
    }

    enum class Mode {
        NONE, DRAG, SCALE
    }

    class Vector(x: Float = 0F, y: Float = 0F) : PointF(x, y) {
        companion object {
            fun getDegree(v1: Vector, v2: Vector): Float {
                return (180.0 / PI * (atan2(v2.y, v2.x) - atan2(v1.y, v1.x))).toFloat()
            }
        }

        constructor(event: MotionEvent) : this() {
            set(event)
        }

        fun set(event: MotionEvent) {
            x = event.getX(1) - event.getX(0)
            y = event.getY(1) - event.getY(0)
            reduction()
        }

        private fun reduction() {
            sqrt(x*x + y*y).also {
                x /= it
                y /= it
            }
        }
    }
}
```