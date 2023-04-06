package com.example.bindservicemessagerpro

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bindservicemessagerpro.databinding.ActivityMainBinding
import java.sql.Connection

class MainActivity : AppCompatActivity() {
    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var serviceConnection: ServiceConnection
    var messenger: Messenger? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //1. 바인딩 서비스를 위해서 ServiceConnection
        serviceConnection = object : ServiceConnection {
            //BindService 요청했을때 연결이 되면 콜백이 진행되고 서비스에 제공된 IBinder를 매개변수로 가져온다.
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                messenger = Messenger(service) //main에서 받음
                Toast.makeText(applicationContext, "Messenger Service 객체받음.", Toast.LENGTH_SHORT)
                    .show()
                binding.btnClick1.isEnabled = false
            }

            // 끊길 시에 보여질 부분
            override fun onServiceDisconnected(name: ComponentName?) {
                Toast.makeText(applicationContext, "Messenger Service 종료", Toast.LENGTH_SHORT)
                    .show()
                binding.btnClick1.isEnabled = true
            }
        }
        //binding service 를 요청한다.
        //위에 있는 커낵션이
        binding.btnClick1.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            //service: IBinder 를 던져줘야함/ 서비스 동작 NO? ->  binder 로 연결 요청.
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        //IBinder 매개변수를 통해 전달해온 메신저를 통해 데이터, 명령을 서비스에 전달한다.
        binding.btnClick2.setOnClickListener {
            if (messenger != null) {
                val message = Message()
                message.what = 100
                message.obj = "hello world!"
                messenger?.send(message)
            }
        }
    }
}