// THIS IS AN AUTO-GENERATED FILE. DO NOT MODIFY.    
// generated by GenCounter on Sat Jan 17 14:49:38 PST 2009
package scalanlp.counters.floats;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type Float with type Long.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait Float2LongCounter extends LongCounter[Float] {


  abstract override def update(k : Float, v : Long) = {

    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : Float, v : Long) :Option[Long] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : Float) = {

    super.-=(key);
  }

  /**
   * Increments the count by the given parameter.
   */
   override  def incrementCount(t : Float, v : Long) = {
     update(t,(this(t) + v).asInstanceOf[Long]);
   }


  override def ++=(kv: Iterable[(Float,Long)]) = kv.foreach(+=);

  /**
   * Increments the count associated with Float by Long.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: (Float,Long)) = incrementCount(kv._1,kv._2);

  override def default(k : Float) : Long = 0;

  override def apply(k : Float) : Long = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): Float2LongCounter  = super.clone().asInstanceOf[Float2LongCounter]

  /**
   * Return the Float with the largest count
   */
  override  def argmax() : Float = (elements reduceLeft ((p1:(Float,Long),p2:(Float,Long)) => if (p1._2 > p2._2) p1 else p2))._1

  /**
   * Return the Float with the smallest count
   */
  override  def argmin() : Float = (elements reduceLeft ((p1:(Float,Long),p2:(Float,Long)) => if (p1._2 < p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
  override  def max : Long = values reduceLeft ((p1:Long,p2:Long) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
  override  def min : Long = values reduceLeft ((p1:Long,p2:Long) => if (p1 < p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
  override  def comparator(a : Float, b :Float) = apply(a) compare apply(b);

  /**
   * Return a new Float2DoubleCounter with each Long divided by the total;
   */
  override  def normalized() : Float2DoubleCounter = {
    val normalized = Float2DoubleCounter();
    val total : Double = this.total
    if(total != 0.0)
      for (pair <- elements) {
        normalized(pair._1) = pair._2 / total;
      }
    normalized
  }

  /**
   * Return the sum of the squares of the values
   */
  override  def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

  /**
   * Return a List the top k elements, along with their counts
   */
  override  def topK(k : Int) = Counters.topK[(Float,Long)](k,(x,y) => if(x._2 < y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : Float2LongCounter) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : Float2LongCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Long]);
    }
  }

  def -=(that : Float2LongCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) - v).asInstanceOf[Long]);
    }
  }

  override  def *=(scale : Long) {
    transform { (k,v) => (v * scale).asInstanceOf[Long]}
  }

  override  def /=(scale : Long) {
    transform { (k,v) => (v / scale).asInstanceOf[Long]}
  }
}


object Float2LongCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._


  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter extends MapWrapper[Float,Long] with Float2LongCounter {
    private val under = new Float2LongOpenHashMap;
    def underlying() = under.asInstanceOf[java.util.Map[Float,Long]];
    override def apply(x : Float) = under.get(x);
    override def update(x : Float, v : Long) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  def apply() = new FastMapCounter();

  
}

