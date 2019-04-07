package kevinlee.test

import java.io.{BufferedWriter, File, FileWriter}
import java.nio.file.Files

import kevinlee.test.data.{Content, Names, NamesAndContent}

import scala.annotation.tailrec

/**
  * @author Kevin Lee
  * @since 2019-02-23
  */
object IoUtil {

  def withTempDir[A](f: File => A): A = {
    lazy val tmp = Files.createTempDirectory("IoUtil_temp").toFile
    try {
      f(tmp)
    } finally {
      cleanAll(tmp)
    }
  }

  def cleanAll(dir: File): Unit = {
    @tailrec
    def getAllFiles(files: List[File], acc: List[File]): List[File] = files match {
      case x :: xs =>
        if (x.isDirectory) {
          getAllFiles(x.listFiles.toList, x :: acc)
        } else {
          getAllFiles(xs, x :: acc)
        }
      case Nil =>
        acc
    }
    getAllFiles(List(dir), Nil).foreach(_.delete())
  }

  def writeFile(file: File, content: String): Unit = {
    val parentFile = file.getParentFile
    if (!parentFile.exists()) {
      parentFile.mkdirs()
    }

    val writer = new BufferedWriter(new FileWriter(file))
    try {
      writer.write(content)
    } finally {
      writer.close()
    }
  }

  def readFile(file: File): String =
    scala.io.Source.fromFile(file).mkString


  def createFiles(rootDir: File, namesAndContentList: List[NamesAndContent]): List[(String, File)] = {
    for {
      NamesAndContent(Names(names), Content(content)) <- namesAndContentList
      path = names.mkString("/")
      file = new File(rootDir, path)
      _ = IoUtil.writeFile(file, content)
    } yield (path, file)
  }
}
