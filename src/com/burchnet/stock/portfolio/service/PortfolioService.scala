package com.burchnet.stock.portfolio.service

import com.burchnet.stock.portfolio.domain._
import com.burchnet.stock.portfolio.service._
import com.burchnet.stock.portfolio.repository.PortfolioRepository

class PortfolioService(repo: PortfolioRepository,
                        processor: PortfolioProcessor,
                        stockInfoService: StockInfoService,
                        brokerService: BrokerService) {
  
  
  def optimize(desiredPortfolioChanges: Map[String, StockChangeRequest]){
    val portfolio = repo.retrivePortfolio
    val totalValueOfPortfolio = stockInfoService.calculateTotal(portfolio)
    println(s"Total value is: $totalValueOfPortfolio")
    val brokerActions = calculateChanges(desiredPortfolioChanges, portfolio, totalValueOfPortfolio) ++ calculateRemovals(desiredPortfolioChanges, portfolio)
    val newPortfolio = brokerService.managePortfolioChanges(brokerActions)
    println("-------Current Portfolio-------")
    newPortfolio.foreach(stock => println(s"${stock.symbol}: ${stock.numberOfShares}"))
  }

  //This will calculate all changes
  private def calculateChanges(desiredPortfolioChanges: Map[String,StockChangeRequest], portfolio: Portfolio, totalValueOfPortfolio: Double): List[BrokerAction] = {
    desiredPortfolioChanges.foldLeft(List.empty[BrokerAction])((result, stockChange) =>{
      val(symbol, change) = stockChange //stockChange is a key value pair object this notation splits that into two variables for clarity
      
      //Next few lines of code finds the stock between the stockInfo service and the portfolio. 
      //If the stock can't be found in the service an error is returned 
      findStock(symbol, change.percentageAllowed, portfolio, (stockInfoService.retrieve _)) match{
        case Right(stockDetail) =>  processor.process(stockDetail, totalValueOfPortfolio) :: result
        case Left(error) =>
          println(error)
          result
      }
    })
  }
  
  //This method handles the scenario where the request doesn't have info for the stock that is in the portfolio. Which the stock will be fully stold
  private def calculateRemovals(desiredPortfolioChanges: Map[String,StockChangeRequest], portfolio: Portfolio): List[BrokerAction] = {
    portfolio.stocks.foldLeft(List.empty[BrokerAction])((result, stock) =>{
      desiredPortfolioChanges.get(stock.symbol) match {
        case None => Sell(stock.numberOfShares, stock) :: result
        case _ => result
      }
    })
  }
  
  /*
   * Finds the stock detail from the portfolio and from the StockInfoService
   */
  private def findStock(symbol: String, percentageAllowed: Double,  portfolio: Portfolio, stockInfoFinder: String => Option[StockInfo]): Either[String, StockDetail] = {
      stockInfoFinder(symbol) match{
        case Some(stockInfo) =>
          val stock = portfolio.stocks.find(_.symbol == symbol).getOrElse(Stock(symbol, 0))
          Right(StockDetail(symbol, percentageAllowed, stockInfo.price, stock.numberOfShares))
        case None => Left("Failed To Find Stock")
      }
  }
}