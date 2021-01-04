package com.isoftinc.film.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.isoftinc.film.R
import com.isoftinc.film.fragment.DetailFragment
import com.isoftinc.film.util.BaseActivity

class Movies2HorizontalAdapter(val activity: BaseActivity, val pos:Int) : RecyclerView.Adapter<Movies2HorizontalAdapter.HorizontalViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_list_item, parent, false)
        return HorizontalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {

        when(pos){
            0 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 210
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            1 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            2 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            3 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            4 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            5 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            6 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }
            7 -> {
                when(position){
                    0 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    1 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    2 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    3 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    4 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    5 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    6 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    7 -> {
                        holder.cardImage.setImageResource(R.drawable.home_12)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    8 -> {
                        holder.cardImage.setImageResource(R.drawable.home_13)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                    9 -> {
                        holder.cardImage.setImageResource(R.drawable.home_11)
                        holder.cardImage.layoutParams.width = 250
                        holder.cardImage.layoutParams.height = 350
                    }
                }
            }

        }


        holder.cardImage.setOnClickListener {
            activity.replaceFragment(
                DetailFragment.newInstance(true,null)
            )
        }
    }

    override fun getItemCount(): Int {
        return 8
    }

    inner class HorizontalViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cardImage: ImageView
        // var cardTitle: TextView

        init {
            cardImage = itemView.findViewById(R.id.image)
            //   cardTitle = itemView.findViewById(R.id.text)
        }
    }
}