package com.burchnet.stock.portfolio.domain

case class StockDetail(symbol: String, allowedPercentage: Double, currentPrice: Double, numberOfShares: Int)