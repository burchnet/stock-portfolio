package com.burchnet.stock.portfolio.domain

abstract class BrokerAction(number: Int, stock: Stock)

case class Buy(number: Int, stock: Stock) extends BrokerAction(number, stock)
case class Sell(number: Int, stock: Stock) extends BrokerAction(number, stock)
case class Nothing(stock: Stock) extends BrokerAction(0, stock)