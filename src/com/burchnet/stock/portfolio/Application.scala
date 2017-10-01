package com.burchnet.stock.portfolio

import com.burchnet.stock.portfolio.service.PortfolioService
import com.burchnet.stock.portfolio.repository.PortfolioRepository
import com.burchnet.stock.portfolio.service.PortfolioProcessor
import com.burchnet.stock.portfolio.service.StockInfoService
import com.burchnet.stock.portfolio.service.BrokerService
import com.burchnet.stock.portfolio.domain.StockChangeRequest

object Application {
  val service = new PortfolioService(new PortfolioRepository(), new PortfolioProcessor, new StockInfoService, new BrokerService)
  def main(args: Array[String]){
    val desiredPortfolioChanges = Map("AAPL" -> StockChangeRequest("AAPL", 0.22),
        "GOOG" -> StockChangeRequest("GOOG", 0.38),
        "GFN" -> StockChangeRequest("GFN", 0.25),
        "ACAD" ->StockChangeRequest("ACAD", 0.15))
        
    service.optimize(desiredPortfolioChanges)
  }
}