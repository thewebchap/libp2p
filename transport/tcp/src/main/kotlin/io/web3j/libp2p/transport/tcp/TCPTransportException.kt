/*
 * Copyright 2019 BLK Technologies Limited (web3labs.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.web3j.libp2p.transport.tcp

import io.web3j.libp2p.shared.env.CodeWithReason

/**
 * Represents an exception relating to the TCP transport layer.
 */
class TCPTransportException : Exception {
    val codeWithReason: CodeWithReason

    constructor(message: String, codeWithReason: CodeWithReason) : super(message) {
        this.codeWithReason = codeWithReason
    }

    constructor(codeWithReason: CodeWithReason) : super(codeWithReason.reason) {
        this.codeWithReason = codeWithReason
    }
}
