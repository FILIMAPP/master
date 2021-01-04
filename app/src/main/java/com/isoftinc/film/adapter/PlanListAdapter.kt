package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.databinding.LayoutPlanListAdapterBinding
import com.isoftinc.film.databinding.LayoutTitleAdapterBinding
import com.isoftinc.film.model.PlanList
import com.isoftinc.film.util.BaseActivity

class PlanListAdapter  : RecyclerView.Adapter<PlanListAdapter.ViewHolder>() {
    private var onDialog: OnListener? = null
    var clickPosition = -1
    var activity: BaseActivity? = null
    var  planList : ArrayList<PlanList>? = null
    var id = ""
    var isFirstTime = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutTitleAdapterBinding = DataBindingUtil.inflate<LayoutPlanListAdapterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_plan_list_adapter, parent, false
        )
        return ViewHolder(layoutTitleAdapterBinding)
    }


    interface OnListener {
        fun accept(pos : Int)
    }


    fun setListner(onSuccess: OnListener) {
        this.onDialog = onSuccess
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = planList!![position]
        holder.layoutTitleAdapterBinding.price.text = "\u20B9" + model.price + "/-"
        holder.layoutTitleAdapterBinding.month.text = model.month + " Months"
      //  holder.layoutTitleAdapterBinding.rbplan.text = model.month +" month:Rs." + model.price +" /-"

      /*  if(model.price == "189.00"){
            clickPosition == position
            holder.layoutTitleAdapterBinding.rbplan.isChecked = clickPosition == position
        }else{
            holder.layoutTitleAdapterBinding.rbplan.isChecked = clickPosition == position
        }*/


        if(id.isNullOrEmpty() && model.price == "189.00" && isFirstTime){
            isFirstTime = false
           clickPosition = position
        }else if(id == model._id && isFirstTime){
            isFirstTime = false
           clickPosition = position
        }

            holder.layoutTitleAdapterBinding.rbplan.isChecked = clickPosition == position
        if(clickPosition >= 0){
            if(onDialog != null)
                onDialog!!.accept(clickPosition)
        }


        holder.layoutTitleAdapterBinding.rbplan.setOnClickListener {
          clickPosition = position
            notifyDataSetChanged()
            if(onDialog != null)
                onDialog!!.accept(position)

        }



    }



    override fun getItemCount(): Int {
        return planList?.size ?: 0
    }

    fun setPlanList( activity: BaseActivity,planList : ArrayList<PlanList>?, id:String?){
        this.activity = activity
        this.planList = planList
        this.id = id!!
        notifyDataSetChanged()

    }

    inner class ViewHolder(val layoutTitleAdapterBinding: LayoutPlanListAdapterBinding) :
        RecyclerView.ViewHolder(layoutTitleAdapterBinding.root)

}