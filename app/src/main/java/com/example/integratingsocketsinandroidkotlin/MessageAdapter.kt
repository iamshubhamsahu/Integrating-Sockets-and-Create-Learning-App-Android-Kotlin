package com.example.integratingsocketsinandroidkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import org.json.JSONException
import org.json.JSONObject

class MessageAdapter(private val inflater: LayoutInflater, private val userName: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MESSAGE_SENT = 0
        private const val TYPE_MESSAGE_RECEIVED = 1
        private const val TYPE_IMAGE_SENT = 2
        private const val TYPE_IMAGE_RECEIVED = 3
    }


    private val messages = ArrayList<JSONObject>()

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTxt: TextView = itemView.findViewById(R.id.sentonlyTxt)
    }

    inner class SentImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.sendonlyImage)
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        val messageTxt: TextView = itemView.findViewById(R.id.receivedTxt)
    }

    inner class ReceivedImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTxt: TextView = itemView.findViewById(R.id.receivedName)
        val imageView: ImageView = itemView.findViewById(R.id.receivedImage)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        try {
            return if (message.getBoolean("isSent")) {
                if (message.has("message")) TYPE_MESSAGE_SENT
                else TYPE_IMAGE_SENT
            } else {
                if (message.has("message")) TYPE_MESSAGE_RECEIVED
                else TYPE_IMAGE_RECEIVED
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return -1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {

            TYPE_MESSAGE_SENT -> {
                view = inflater.inflate(R.layout.item_sent_message, parent, false)
                SentMessageHolder(view)
            }

            TYPE_MESSAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.item_received_message, parent, false)
                ReceivedMessageHolder(view)
            }

            TYPE_IMAGE_SENT -> {
                view = inflater.inflate(R.layout.item_sent_image, parent, false)
                SentImageHolder(view)
            }

            TYPE_IMAGE_RECEIVED -> {
                view = inflater.inflate(R.layout.item_received_photo, parent, false)
                ReceivedImageHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        try {
            if (message.getBoolean("isSent")) {
                if (message.has("message")) {
                    val messageHolder = holder as SentMessageHolder
                    messageHolder.messageTxt.text = message.getString("message")
                } else {
                    val imageHolder = holder as SentImageHolder
                    val bitmap = getBitmapFromString(message.getString("image"))
                    imageHolder.imageView.setImageBitmap(bitmap)
                }
            } else {
                if (message.has("message")) {
                    val messageHolder = holder as ReceivedMessageHolder
                    //Duplicate msg issue work here
                    messageHolder.nameTxt.text = message.getString("name")
                    messageHolder.messageTxt.text = message.getString("message")
                    if (!messageHolder.nameTxt.text.equals(userName)) {
                        messageHolder.nameTxt.visibility =View.VISIBLE
                        messageHolder.messageTxt.visibility = View.VISIBLE
                    }
                } else {
                    val imageHolder = holder as ReceivedImageHolder
                    imageHolder.nameTxt.text = message.getString("name")
                    val bitmap = getBitmapFromString(message.getString("image"))
                    imageHolder.imageView.setImageBitmap(bitmap)
                    if (!imageHolder.nameTxt.text.equals(userName)) {
                        imageHolder.nameTxt.visibility =View.VISIBLE
                        imageHolder.imageView.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromString(image: String): Bitmap? {
        val bytes = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun addItem(jsonObject: JSONObject) {
        messages.add(jsonObject)
        notifyDataSetChanged()
    }
}