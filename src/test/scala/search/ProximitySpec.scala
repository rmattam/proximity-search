package search

import org.scalatest._

import scala.collection.mutable.ArrayBuffer

class ProximitySpec extends FlatSpec with Matchers {
  val input = "Doc1 breakthrough drug for schizophrenia\nDoc2 new schizophrenia drug\nDoc3 new drug for treatment of schizophrenia\nDoc4 new hopes for schizophrenia patients"
  val test1 = "hello world"
  val index = new PositionalIndex()
  index.build_from_string(input)



  "A Proximity Search Index" should "return the proximity intersection for query 1" in {
      val result = ArrayBuffer[(String, Int, Int)](
        ("Doc1",4, 2),
        ("Doc2",2, 3))

     index.search("schizophrenia /2 drug") should be (result)
  }

  "A Proximity Search Index" should "return the proximity intersection for query 2" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",4, 2),
      ("Doc2",2, 3),
      ("Doc3",6, 2))

    index.search("schizophrenia /4 drug") should be (result)
  }
}