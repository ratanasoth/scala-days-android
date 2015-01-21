/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.utils

import java.io.{Closeable, InputStream, File}

import macroid.AppContext

import scala.util.Try

object FileUtils {
  
  def getJsonAssets(fileName: String)(implicit appContext: AppContext): Try[String] =
    Try {
      withResource[InputStream, String](appContext.get.getResources.getAssets.open(fileName)) {
        inputStream => scala.io.Source.fromInputStream(inputStream).mkString
      }
    }
  
  def getJsonCache(fileName: String)(implicit appContext: AppContext): Try[String] =
    Try {
      withResource[File, String](appContext.get.getCacheDir) {
        cacheDir => scala.io.Source.fromFile(new File(cacheDir, fileName), "UTF-8").mkString
      }
    }
  
  private def withResource[C <: Closeable, R](closeable: C)(f: C => R) = {
    import scala.util.control.Exception._
    allCatch.andFinally{ closeable.close() } apply { f(closeable) }
  }
}
