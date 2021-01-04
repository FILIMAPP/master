package com.isoftinc.film.model

import java.util.ArrayList

class PlanListModel(
    val code:Int,
    val result:PlanResultModel
)

class PlanResultModel(
  val plan_list : ArrayList<PlanList>
)

class  PlanList(
   var price:String,
   var month :String,
   var _id :String,
)

