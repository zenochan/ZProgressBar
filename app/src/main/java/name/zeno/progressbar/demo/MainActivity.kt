package name.zeno.progressbar.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    sb.max = 100
    sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        pb.process = p1 / 100F
        pb.text = "$p1/100"
      }

      override fun onStartTrackingTouch(p0: SeekBar?) {}
      override fun onStopTrackingTouch(p0: SeekBar?) {}
    })

    rgGravity.setOnCheckedChangeListener { _, id ->
      when (id) {
        rbLeft.id -> pb.gravity = Gravity.LEFT
        rbCenter.id -> pb.gravity = Gravity.CENTER
        rbRight.id -> pb.gravity = Gravity.RIGHT
      }
    }
  }
}
