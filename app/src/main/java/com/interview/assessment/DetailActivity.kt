package com.interview.assessment

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // 初始化所有 UI 控件
        val ivDetailAvatar = findViewById<ImageView>(R.id.ivDetailAvatar)
        val tvDetailName = findViewById<TextView>(R.id.tvDetailName)
        val tvDetailSpecies = findViewById<TextView>(R.id.tvDetailSpecies)
        val tvDetailStatus = findViewById<TextView>(R.id.tvDetailStatus)

        // 新增的控件
        val tvDetailGender = findViewById<TextView>(R.id.tvDetailGender)
        val tvDetailOrigin = findViewById<TextView>(R.id.tvDetailOrigin)
        val tvDetailLocation = findViewById<TextView>(R.id.tvDetailLocation)
        val tvDetailEpisodes = findViewById<TextView>(R.id.tvDetailEpisodes)

        val characterId = intent.getIntExtra("CHARACTER_ID", -1)

        if (characterId != -1) {
            lifecycleScope.launch {
                try {
                    val character = RetrofitClient.api.getCharacterDetail(characterId)

                    // 绑定基础数据
                    tvDetailName.text = character.name
                    tvDetailSpecies.text = "物种: ${character.species}"
                    tvDetailStatus.text = "状态: ${character.status}"

                    // 绑定新增数据
                    tvDetailGender.text = "🚻 性别: ${character.gender}"
                    tvDetailOrigin.text = "🌍 起源地: ${character.origin.name}"
                    tvDetailLocation.text = "📍 最后位置: ${character.location.name}"
                    // episode 是个数组（里面装的是 URL），我们直接取它的 length 显示出场集数
                    tvDetailEpisodes.text = "🎬 出场集数: 共 ${character.episode.size} 集"

                    // 设置状态颜色
                    when (character.status.lowercase()) {
                        "alive" -> {
                            tvDetailStatus.setTextColor(Color.parseColor("#2E7D32"))
                            tvDetailStatus.background.setTint(Color.parseColor("#E8F5E9"))
                        }
                        "dead" -> {
                            tvDetailStatus.setTextColor(Color.parseColor("#C62828"))
                            tvDetailStatus.background.setTint(Color.parseColor("#FFEBEE"))
                        }
                        else -> {
                            tvDetailStatus.setTextColor(Color.parseColor("#EF6C00"))
                            tvDetailStatus.background.setTint(Color.parseColor("#FFF3E0"))
                        }
                    }

                    // 加载大图
                    Glide.with(this@DetailActivity)
                        .load(character.image)
                        .into(ivDetailAvatar)

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, "加载详情失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}