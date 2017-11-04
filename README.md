# CustomView
a simple example of a custom view
with support to XML attributes and image src

![Main Screen](images/Screenshot_Main.png)

### Usage

VolumeView

``` xml

    <com.example.customview.VolumeView
        android:layout_width="200dp"
        android:layout_height="75dp" />

```

``` java

    volumeView.increase();
	volumeView.decrease();

```

ArcView

``` xml

    <com.example.customview.ArcView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

```

``` java

	arcView.setDestAngle(160);

```

CircleView

``` xml

    <com.example.customview.CircleView
        android:layout_width="80dp"
        android:layout_height="80dp"
        app:circle_bottom_color="@color/red"
        app:circle_top_color="@color/purple"
        app:circle_stroke_width="10dp" />

```

TriangleView

``` xml

    <com.example.customview.TriangleView
        android:layout_width="120dp"
        android:layout_height="80dp"
        app:triangle_color="@color/purple" />


```