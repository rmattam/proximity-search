package search

import org.scalatest._

import scala.collection.mutable.ArrayBuffer

class ProximitySpec extends FlatSpec with Matchers {
  val input = "Doc1 breakthrough drug for schizophrenia\nDoc2 new schizophrenia drug\nDoc3 new drug for treatment of schizophrenia\nDoc4 new hopes for schizophrenia patients"
  val test1 = "hello world"
  val index = new PositionalIndex()
  index.build_from_string(input)



  "A Proximity Search Index" should "return the proximity intersection for non-directional query 1" in {
      val result = ArrayBuffer[(String, Int, Int)](
        ("Doc1",4, 2),
        ("Doc2",2, 3))

     index.search("schizophrenia /2 drug", false) should be (result)
  }

  it should "return the proximity intersection for non-directional query 2" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",4, 2),
      ("Doc2",2, 3),
      ("Doc3",6, 2))

    index.search("schizophrenia /4 drug", false) should be (result)
  }

  it should "return the proximity intersection for non-directional query 3" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc2",2, 3))

    index.search("schizophrenia /1 drug", false) should be (result)
  }

  it should "return the proximity intersection for non-directional query 4" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",2, 4),
      ("Doc2",3, 2),
      ("Doc3",2, 6)
    )

    index.search("drug /4 schizophrenia") should be (result)
  }

  it should "return the proximity intersection for directional query 1" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc2",2, 3))

    index.search("schizophrenia /2 drug", true) should be (result)
  }

  it should "return the proximity intersection for directional query 2" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc2",2, 3))

    index.search("schizophrenia /4 drug", true) should be (result)
  }

  it should "return the proximity intersection for directional query 3" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",2, 4),
      ("Doc3",2, 6)
    )

    index.search("drug /4 schizophrenia", true) should be (result)
  }

  it should "return the proximity intersection for directional query 4" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",2, 4)
    )

    index.search("drug /2 schizophrenia", true) should be (result)
  }

  it should "return the proximity intersection for directional query 5" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc2",2, 3))

    index.search("schizophrenia /1 drug", true) should be (result)
  }

  it should "return the proximity intersection for directional query 6" in {
    val result = ArrayBuffer[(String, Int, Int)]()

    index.search("drug /1 schizophrenia", true) should be (result)
  }

  it should "return the proximity intersection for directional query 7" in {
    val result = ArrayBuffer[(String, Int, Int)](
      ("Doc1",2, 4),
      ("Doc3",2, 6)
    )

    index.search("drug /4 schizophrenia -d") should be (result)
  }
}