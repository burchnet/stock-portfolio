package com.burchnet.stock.portfolio.service

import com.burchnet.stock.portfolio.domain.{StockInfo, Portfolio}

/*
 * AAPL	154.12
GOOG	959.11
CYBR	41
ABB	24.75
GFN	5.05
ACAD	37.67
 * 
 * 
 * */
class StockInfoService {
    private val dataStore: Map[String, Double] = 
      Map("AAPL" -> 154.12, "GOOG" ->959.11, "CYBR"->41, "ABB" -> 24.75, "GFN"->5.05, "ACAD"-> 37.67)
      
    def retrieve(symbol: String): Option[StockInfo] = {
      dataStore.get(symbol).map(price => StockInfo(symbol, price))
    }
    
    def calculateTotal(portfolio: Portfolio): Double = {
      portfolio.stocks.foldLeft(0D)((total, next) => total + next.numberOfShares * dataStore.get(next.symbol).getOrElse(0D));
    }
}