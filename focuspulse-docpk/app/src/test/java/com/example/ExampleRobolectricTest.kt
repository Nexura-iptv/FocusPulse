package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.audio.AmbientSoundType
import com.example.model.CourseRepository
import com.example.model.SubjectBranch
import com.example.model.YouTubeDirectory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FocusPulse", appName)
  }

  @Test
  fun `verify course repository contains all grade levels and requested exams`() {
    val grades = CourseRepository.gradeLevels.map { it.id }
    // 1-12 grades
    (1..12).forEach { gradeNum ->
      assertTrue("Missing grade: $gradeNum", grades.contains(gradeNum.toString()))
    }
    // YKS TYT, AYT, KPSS
    assertTrue(grades.contains("YKS_TYT"))
    assertTrue(grades.contains("YKS_AYT"))
    assertTrue(grades.contains("KPSS"))
  }

  @Test
  fun `verify branch filtering works`() {
    val mathCourses = CourseRepository.getCourses("", SubjectBranch.MATHEMATICS)
    assertTrue(mathCourses.isNotEmpty())
    mathCourses.forEach {
      assertEquals(SubjectBranch.MATHEMATICS, it.branch)
    }
  }

  @Test
  fun `verify ambient sound types contains all requested sounds`() {
    val soundTypes = AmbientSoundType.values().map { it.id }
    assertTrue(soundTypes.contains("rain"))
    assertTrue(soundTypes.contains("gamma_40hz"))
    assertTrue(soundTypes.contains("white_noise"))
    assertTrue(soundTypes.contains("library"))
    assertTrue(soundTypes.contains("fireplace"))
  }

  @Test
  fun `verify quick access youtube channels are configured`() {
    val channels = YouTubeDirectory.quickAccessChannels.map { it.name }
    assertTrue(channels.contains("Hocalara Geldik"))
    assertTrue(channels.contains("Benim Hocam"))
    assertTrue(channels.contains("Rehber Matematik"))
    assertTrue(channels.contains("Tonguç Akademi"))
    assertTrue(channels.contains("Bıyıklı Matematik"))
  }

  @Test
  fun `verify youtube video id extractor`() {
    val url1 = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    val url2 = "https://youtu.be/dQw4w9WgXcQ"
    val idOnly = "dQw4w9WgXcQ"

    assertEquals("dQw4w9WgXcQ", YouTubeDirectory.extractVideoId(url1))
    assertEquals("dQw4w9WgXcQ", YouTubeDirectory.extractVideoId(url2))
    assertEquals("dQw4w9WgXcQ", YouTubeDirectory.extractVideoId(idOnly))
  }
}
