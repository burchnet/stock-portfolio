package com.burchnet.stock.portfolio.repository

import com.burchnet.stock.portfolio.domain._

class PortfolioRepository {
  def retrivePortfolio: Portfolio = {
      Portfolio(Stock("AAPL", 50) ::
          Stock("GOOG", 200) ::
          Stock("CYBR", 150) ::
          Stock("ABB", 900) :: Nil)
  }
}