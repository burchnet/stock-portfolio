package com.burchnet.stock.portfolio.domain

case class StockChangeRequest(symbol: String, percentageAllowed: Double)