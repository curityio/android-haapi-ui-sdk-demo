/*
 *  Copyright 2023 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.curity.haapidemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.curity.haapidemo.authenticated.AuthenticatedActivity
import io.curity.haapidemo.databinding.ActivityMainBinding
import se.curity.identityserver.haapi.android.ui.widget.HaapiFlowActivity
import se.curity.identityserver.haapi.android.ui.widget.models.OauthModel

class MainActivity : AppCompatActivity() {
    private lateinit var haapiFlowLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        haapiFlowLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.button.setLoading(false)
            if (it.resultCode == Activity.RESULT_OK) {
                val tokenResponse = it.data?.getParcelableExtra(HaapiFlowActivity.className) as OauthModel.Token?
                val userInfoURI = (application as DemoApplication).configuration.userInfoURI
                val authenticatedActivity = AuthenticatedActivity.newIntent(this, userInfoURI, tokenResponse!!)
                startActivity(authenticatedActivity)
            }
        }
    }

    fun onStartAuthorizationClick(view: View) {
        binding.button.setLoading(true)
        haapiFlowLauncher.launch(HaapiFlowActivity.newIntent(this))
    }
}
