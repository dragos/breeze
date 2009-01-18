// THIS IS AN AUTO-GENERATED FILE. DO NOT MODIFY.    
// generated by GenCounter on Sat Jan 17 14:49:38 PST 2009
package scalanlp.counters;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type T with type Double.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait DoubleCounter[T] extends Map[T,Double] {

  private var pTotal: Double = 0;

  /**
   * Return the sum of all values in the map.
   */
  def total() = pTotal;

  final protected def updateTotal(delta : Double) {
    pTotal += delta;
  }

  override def clear() {
    pTotal = 0;
    super.clear();
  }


  abstract override def update(k : T, v : Double) = {
    updateTotal(v - this(k))
    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : T, v : Double) :Option[Double] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : T) = {

    updateTotal(-this(key))

    super.-=(key);
  }

  /**
   * Increments the count by the given parameter.
   */
    def incrementCount(t : T, v : Double) = {
     update(t,(this(t) + v).asInstanceOf[Double]);
   }


  override def ++=(kv: Iterable[(T,Double)]) = kv.foreach(+=);

  /**
   * Increments the count associated with T by Double.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: (T,Double)) = incrementCount(kv._1,kv._2);

  override def default(k : T) : Double = 0;

  override def apply(k : T) : Double = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): DoubleCounter[T]  = super.clone().asInstanceOf[DoubleCounter[T]]

  /**
   * Return the T with the largest count
   */
   def argmax() : T = (elements reduceLeft ((p1:(T,Double),p2:(T,Double)) => if (p1._2 > p2._2) p1 else p2))._1

  /**
   * Return the T with the smallest count
   */
   def argmin() : T = (elements reduceLeft ((p1:(T,Double),p2:(T,Double)) => if (p1._2 < p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
   def max : Double = values reduceLeft ((p1:Double,p2:Double) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
   def min : Double = values reduceLeft ((p1:Double,p2:Double) => if (p1 < p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
   def comparator(a : T, b :T) = apply(a) compare apply(b);

  /**
   * Return a new DoubleCounter[T] with each Double divided by the total;
   */
   def normalized() : DoubleCounter[T] = {
    val normalized = DoubleCounter[T]();
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
   def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

  /**
   * Return a List the top k elements, along with their counts
   */
   def topK(k : Int) = Counters.topK[(T,Double)](k,(x,y) => if(x._2 < y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : DoubleCounter[T]) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : DoubleCounter[T]) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Double]);
    }
  }

  def -=(that : DoubleCounter[T]) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) - v).asInstanceOf[Double]);
    }
  }

   def *=(scale : Double) {
    transform { (k,v) => (v * scale).asInstanceOf[Double]}
  }

   def /=(scale : Double) {
    transform { (k,v) => (v / scale).asInstanceOf[Double]}
  }
}


object DoubleCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._

  import scalanlp.counters.ints._
  import scalanlp.counters.shorts._
  import scalanlp.counters.longs._
  import scalanlp.counters.floats._
  import scalanlp.counters.doubles._


  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter[T] extends MapWrapper[T,Double] with DoubleCounter[T] {
    private val under = new Object2DoubleOpenHashMap[T];
    def underlying() = under.asInstanceOf[java.util.Map[T,Double]];
    override def apply(x : T) = under.getDouble(x);
    override def update(x : T, v : Double) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  def apply[T]() = new FastMapCounter[T]();

  
  private def runtimeClass[T](x : Any) = x.asInstanceOf[AnyRef].getClass

  private val INT = runtimeClass(3);
  private val LNG = runtimeClass(3l);
  private val FLT = runtimeClass(3.0f);
  private val SHR = runtimeClass(3.asInstanceOf[Short]);
  private val DBL = runtimeClass(3.0);

  def apply[T](x : T) : DoubleCounter[T] = {
    runtimeClass(x) match {
      case INT => Int2DoubleCounter().asInstanceOf[DoubleCounter[T]];
      case DBL => Double2DoubleCounter().asInstanceOf[DoubleCounter[T]];
      case FLT => Float2DoubleCounter().asInstanceOf[DoubleCounter[T]];
      case SHR => Short2DoubleCounter().asInstanceOf[DoubleCounter[T]];
      case LNG => Long2DoubleCounter().asInstanceOf[DoubleCounter[T]];
      case _ => DoubleCounter().asInstanceOf[DoubleCounter[T]];
    }
  }
      
}

