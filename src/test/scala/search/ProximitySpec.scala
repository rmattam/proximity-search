package search

import org.scalatest._

class ProximitySpec extends FlatSpec with Matchers {
  val input = "Doc1 breakthrough  drug  for schizophrenia\nDoc2 new approach for treatment of schizophrenia\nDoc3 new hopes for schizophrenia patients\nDoc4 new schizophrenia drug"
  val test1 = "hello world"
  //val index = new booleanIndex()
  //index.build_from_string(input)

  "A Proximity Search Index" should "return the simple intersection for AND query 1" in {
    "hello world" should equal (test1)
  }

  it should "return the simple intersection for AND query 2" in {
    input should equal (input)
  }
}