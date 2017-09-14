# ZProgressBar

> 带文本进度条，文字颜色随进度改变

![demo](./screenshot/demo.jpg)

## [attrs](./library/src/main/res/values/attrs_ZprogressBar.xml)

## Thanks

- [ Android 自定义View-图片文字变色,实现酷炫LoadingView或者进度条](http://blog.csdn.net/u014702653/article/details/51999179)
  ```java
  FontMetricsInt fm = mPaint.getFontMetricsInt();
  int startY = getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2;
  ```
