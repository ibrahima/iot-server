package io.ibrahim.iot.algorithms

import breeze.linalg._
import breeze.numerics._
import breeze.stats.distributions._
import io.ibrahim.iot._
import io.ibrahim.linalg.Util._

object CompressedSensing  {
  /** Generates a random dictionary matrix for use in compressed sensing
    * applications */
  def gen_a(n: Int, m: Int) = {
    var A = DenseMatrix.rand(n, m)
    for(j <- 0 until m){
      val n = norm(A(::, j))
      A(::, j) := A(::, j)/n
    }
    A
  }

  def gen_x(m: Int, s: Int) = {
    var x = DenseVector.zeros[Double](m)
    val indexes = Rand.randInt(m).samplesVector(s)
    // val N = Rand.uniform.samplesVector(s)
    for (i <- indexes){
      x(i) = Rand.uniform.sample();
    }
    // x(indexes) := N
    x
  }

  def mp(A: DenseMatrix[Double], b: DenseVector[Double]) = {
    val e_0 = 0.0001
    val (n, m) = (A.rows, A.cols);
    var x_hat = DenseVector.zeros[Double](m)
    var e = 100000.0
    var iterations = 1
    var resid = b
    while(abs(e) > e_0){
      val aTb = A.t*resid

      val k_p = argmax(abs(aTb))
      val a_kp = A(::, k_p);

      x_hat(k_p) = x_hat(k_p) + aTb(k_p);
      resid = resid - aTb(k_p) * a_kp
      e = resid.t * resid
      iterations = iterations + 1
    }
    x_hat
  }

  def testCS(s: Int, N_tests: Int) = {
    val n = 30
    val m = 50
    var errors = DenseVector.zeros[Double](N_tests)
    for(i <- 0 until N_tests){
      val A = gen_a(n, m)
      val x = gen_x(m, s)
      val b = A*x
      val x_hat = mp(A, b)

      errors(i) = (x-x_hat).t*(x-x_hat)
    }
    sum(errors)/N_tests
  }

  def handleMPRequest(req: SparseCodingRequest) = {
    s"${req.A} x = ${req.b}"
    val A = csvstring2mat(req.A)
    val b = csvstring2mat(req.b).toDenseVector
    val x_hat = mp(A, b)
    Map("x_hat" -> mat2csvstring(x_hat.toDenseMatrix))
  }
}
