package io.ibrahim.linalg

import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions._
import breeze.io.{CSVWriter, CSVReader}

object Util {
  def mat2csvstring(mat: Matrix[Double]) = {
    CSVWriter.mkString(IndexedSeq.tabulate(mat.rows,mat.cols)(mat(_,_).toString))
  }

  def csvstring2mat(str: String) = {
    val tmp = CSVReader.parse(str)
    DenseMatrix.tabulate(tmp.length,tmp.head.length)((i,j)=>tmp(i)(j).toDouble)
  }

  def mat2String(mat: Matrix[Double]) = {
    IndexedSeq.tabulate(mat.rows,mat.cols)(mat(_,_).toString).toString
  }

}
