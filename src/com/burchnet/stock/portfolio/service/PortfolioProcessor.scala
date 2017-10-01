package com.burchnet.stock.portfolio.service

import com.burchnet.stock.portfolio.domain._

class PortfolioProcessor {
  def process(stock: StockDetail, totalPortfolioValue: Double): BrokerAction = {
     val currentValueOwned = (stock.numberOfShares * stock.currentPrice) 
      
     val allowedValueOwned = totalPortfolioValue * stock.allowedPercentage
     
     val numberOfSharesNeeded = (currentValueOwned - allowedValueOwned)/stock.currentPrice
    
     evaluatAction(numberOfSharesNeeded, stock)
  }

  def evaluatAction(numberOfSharesNeeded: Double, stock: StockDetail) = {
    if(isNumberWholeNumber(numberOfSharesNeeded)){ 
        if(hasMoreThanAllowed(numberOfSharesNeeded)){
          Sell(numberOfSharesNeeded.abs.toInt, Stock(stock.symbol, stock.numberOfShares))
        }else {
          Buy(numberOfSharesNeeded.abs.toInt, Stock(stock.symbol, stock.numberOfShares))
        }
    } else{
      Nothing(Stock(stock.symbol, stock.numberOfShares))
    }
  }
  
  private def isNumberWholeNumber(numberOfSharesNeeded: Double): Boolean = numberOfSharesNeeded.abs >= 1
  private def hasMoreThanAllowed(numberOfSharesNeeded: Double): Boolean = numberOfSharesNeeded > 0
}