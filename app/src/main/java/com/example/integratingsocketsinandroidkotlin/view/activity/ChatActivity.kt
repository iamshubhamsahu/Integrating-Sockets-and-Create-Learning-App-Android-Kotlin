package com.example.integratingsocketsinandroidkotlin.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integratingsocketsinandroidkotlin.adapter.MessageAdapter
import com.example.integratingsocketsinandroidkotlin.R
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class ChatActivity : AppCompatActivity(), TextWatcher {
    private var name: String? = null
    private lateinit var webSocket: WebSocket
    private val SERVER_PATH =
        "wss://s9490.blr1.piesocket.com/v3/1?api_key=745eTnjTjuP2F1WFk1jQwEauoGdg79vtcb240skG&notify_self=1"
    private lateinit var messageEdit: EditText
    private lateinit var sendBtn: View
    private lateinit var pickImgBtn: View
    private lateinit var recyclerView: RecyclerView
    private val IMAGE_REQUEST_ID = 1
    private lateinit var messageAdapter: MessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        name = intent.getStringExtra("name")
        initiateSocketConnection()
    }

    private fun initiateSocketConnection() {
        val client = OkHttpClient()
        val request = Request.Builder().url(SERVER_PATH).build()
        webSocket = client.newWebSocket(request, SocketListener())
    }


    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        val string = p0.toString().trim()

        if (string.isEmpty()) {
            resetMessageEdit()
        } else {
            sendBtn.visibility = View.VISIBLE
            pickImgBtn.visibility = View.INVISIBLE
        }
    }

    private fun resetMessageEdit() {
        messageEdit.removeTextChangedListener(this)

        messageEdit.setText("")
        sendBtn.visibility = View.INVISIBLE
        pickImgBtn.visibility = View.VISIBLE

        messageEdit.addTextChangedListener(this)
    }

    private inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            runOnUiThread {
                Toast.makeText(
                    this@ChatActivity,
                    "Socket Connection Successful!",
                    Toast.LENGTH_SHORT
                ).show()
                initializeView()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)

            runOnUiThread {
                try {
                    val jsonObject = JSONObject(text)
                    jsonObject.put("isSent", false)

                    messageAdapter.addItem(jsonObject)

                    recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initializeView() {
        messageEdit = findViewById(R.id.messageEdit)
        sendBtn = findViewById(R.id.sendBtn)
        pickImgBtn = findViewById(R.id.pickImgBtn)
        recyclerView = findViewById(R.id.recyclerView)


        messageAdapter = MessageAdapter(layoutInflater, name!!)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageEdit.addTextChangedListener(this)

        sendBtn.setOnClickListener {
            sendButton()
        }

        pickImgBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(
                Intent.createChooser(intent, "Pick image"),
                IMAGE_REQUEST_ID
            )
        }
    }

    private fun sendButton() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", name)
            jsonObject.put("message", messageEdit.text.toString())

            webSocket.send(jsonObject.toString())

            jsonObject.put("isSent", true)
            messageAdapter.addItem(jsonObject)

            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)

            resetMessageEdit()

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(data?.data!!)
                val image: Bitmap = BitmapFactory.decodeStream(inputStream)
                sendImage(image)
            }
            catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun sendImage(image: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

        val jsonObject = JSONObject()

        try {
            jsonObject.put("name", name)

            jsonObject.put("image", base64String)

            webSocket.send(jsonObject.toString())

            jsonObject.put("isSent", true)

            messageAdapter.addItem(jsonObject)

            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
