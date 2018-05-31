package test.stockprophet.math;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.stockprophet.common.StockUtil;
import com.stockprophet.common.StockUtil.PriceType;
import com.stockprophet.main.GenerateCommonIndexes;
import com.stockprophet.main.Run;
import com.stockprophet.math.AdvancedFinancialMathMethods;

public class TestAdvancedFinancialMathMethods {
	
	@Test
	public void testOptmizedIntraReturn(){
		HashMap<String, String> indexMap = GenerateCommonIndexes.generateCommonIndexes();
		for(String key : indexMap.keySet()){
			List<Double> open = StockUtil.getPriceFromFile(key, PriceType.OPEN);
			List<Double> high = StockUtil.getPriceFromFile(key, PriceType.HIGH);
			if(open.size() > 0 && high.size() > 0 && open.size() == high.size() && open.size() >= Run.FIVE_YEARS){
				double[] metrics = AdvancedFinancialMathMethods.calculateOptimalAndMaximalIntradayReturns(open, high);
				System.out.println(key + "\t" + metrics[0] + "\t" + metrics[1]);
			}
		}
	}

}
