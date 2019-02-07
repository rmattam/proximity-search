package search

import scala.collection.mutable.{ArrayBuffer, Map}
import scala.util.control.Breaks._

class PositionalIndex {

  /*
  The value of this map is a tuple posting list with the first element storing the document id,
  and the second element stores the positional index in the document having tuple item 1 document id.
  */
  var inverted = Map[String, ArrayBuffer[(Int, ArrayBuffer[Int])]]().withDefaultValue(ArrayBuffer[(Int, ArrayBuffer[Int])]())
  var docID = Map[Int, String]()
  var document_count = 0

  def build_from_string(docs: String): Unit = {
    for (lines:String <- docs.split("\\r?\\n")){
      add(lines)
    }
  }

  def add(lines: String): Unit ={
    val line = lines.split("[ ]+")
    document_count += 1
    docID = docID + (document_count -> line(0))
    for (i <- 1 until line.length){
      val token = line(i)
      if (!inverted.contains(token)) inverted.put(token, ArrayBuffer[(Int, ArrayBuffer[Int])]((document_count, ArrayBuffer[Int]())))
      if(inverted(token).last._1 != document_count) inverted(token).append((document_count, ArrayBuffer[Int]()))
      inverted(token).last._2.append(i)
    }
  }

  def search(input: String, directional: Boolean):ArrayBuffer[(String, Int, Int)] = {

    def closeEnough(first: Int, second: Int, k: Int, directional: Boolean): Boolean = {
      if (directional && first-second > 0){
        return false
      }
      return math.abs(first-second) <= k
    }

    def proximity_intersection(term1: String, term2: String, k: Int, directional: Boolean): ArrayBuffer[(String, Int, Int)] ={
      val result = ArrayBuffer[(String, Int, Int)]()
      var i,j = 0
      val p1 = inverted(term1)
      val p2 = inverted(term2)

      //loop through all matching documents
      while (i < p1.length && j < p2.length){
        if(p1(i)._1 == p2(j)._1){
          //check proximity
          val l = new ArrayBuffer[Int]()
          var pp1, pp2 = 0
          while (pp1 < p1(i)._2.length){
            breakable {
              while (pp2 < p2(j)._2.length) {
                if (closeEnough(p1(i)._2(pp1), p2(j)._2(pp2), k, directional)) {
                  l.append(p2(j)._2(pp2))
                } else if (p2(j)._2(pp2) > p1(i)._2(pp1)) {
                  break
                }
                pp2 += 1
              }
            }

            while(l.length != 0 && math.abs(l(0)-p1(i)._2(pp1)) > k){
              l.remove(0)
            }

            for (ps <- l){
              //actually build our result set
              result.append((docID(p1(i)._1), p1(i)._2(pp1), ps))
            }
            pp1 +=1
          }

          i+=1
          j+=1
        } else if (p1(i)._1 < p2(j)._1){
          i+=1
        } else {
          j+=1
        }
      }
      return result
    }

    val query = input.split("[ ]+")
    val tolerance = query(1).stripPrefix("/").toInt
    return proximity_intersection(query(0), query(2), tolerance, directional)
  }
}
