package io.web3j.libp2p.crypto.keys

import crypto.pb.Crypto
import io.web3j.libp2p.crypto.ErrRsaKeyTooSmall
import io.web3j.libp2p.crypto.Libp2pCrypto
import io.web3j.libp2p.crypto.Libp2pException
import io.web3j.libp2p.crypto.PrivKey
import io.web3j.libp2p.crypto.PubKey
import io.web3j.libp2p.crypto.RSA_ALGORITHM
import io.web3j.libp2p.crypto.RSA_SIGNATURE_ALGORITHM
import io.web3j.libp2p.crypto.marshalPrivateKey
import io.web3j.libp2p.crypto.marshalPublicKey
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.PrivateKey as JavaPrivateKey
import java.security.PublicKey as JavaPublicKey


// RsaPrivateKey is an rsa private key
class RsaPrivateKey(private val sk: JavaPrivateKey, private val pk: JavaPublicKey) : PrivKey {

    private val rsaPublicKey = RsaPublicKey(pk)

    override fun bytes(): ByteArray {
        return marshalPrivateKey(this)
    }

    override fun raw(): ByteArray {
        // sk.format == "PKCS#8" - not sure how to convert this
        // can the format be specified when generating the keypair?
//        b := x509.MarshalPKCS1PrivateKey(sk.sk)
//        return b, nil
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // http://www.java2s.com/Tutorial/Java/0490__Security/BasicclassforexploringPKCS1V15Signatures.htm
//        val cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC")
//        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic())
//
//        val decSig = cipher.doFinal(sigBytes)
//        val aIn = ASN1InputStream(decSig)
//        val seq = aIn.readObject() as ASN1Sequence
//
//        println(ASN1Dump.dumpAsString(seq))
//
//        val hash = MessageDigest.getInstance("SHA-256", "BC")
//        hash.update(message)
//
//        val sigHash = seq.getObjectAt(1) as ASN1OctetString
//        println(MessageDigest.isEqual(hash.digest(), sigHash.octets))

    override fun type(): Crypto.KeyType {
        return Crypto.KeyType.RSA
    }

    override fun sign(data: ByteArray): ByteArray {
        val signature = Signature.getInstance(RSA_SIGNATURE_ALGORITHM, Libp2pCrypto.provider)
        signature.initSign(sk)
        signature.update(data)
        return signature.sign()
    }

    override fun publicKey(): PubKey {
        return rsaPublicKey
    }

}


// RsaPublicKey is an rsa public key
class RsaPublicKey(private val k: JavaPublicKey) : PubKey {
    override fun bytes(): ByteArray {
        return marshalPublicKey(this)
    }

    override fun raw(): ByteArray {
        // FIXME - check this
        // k.format == "x509" not sure if that is equivalent
        return k.encoded
        // return x509.MarshalPKIXPublicKey(pk.k)
    }

    override fun type(): Crypto.KeyType {
        return Crypto.KeyType.RSA
    }

    override fun verify(data: ByteArray, signature: ByteArray): Boolean {
        val signature1 = Signature.getInstance("SHA256withRSA", Libp2pCrypto.provider)
        signature1.initVerify(k)
        signature1.update(data)
        return signature1.verify(signature)
    }

}


/**
 * GenerateRSAKeyPair generates a new rsa private and public key.
 */
fun generateRsaKeyPair(bits: Int): Pair<PrivKey, PubKey> {

    if (bits < 512) {
        throw Libp2pException(ErrRsaKeyTooSmall)
    }


    val kp: KeyPair = with(KeyPairGenerator.getInstance(
        RSA_ALGORITHM,
        Libp2pCrypto.provider
    )) {
        initialize(bits)
        genKeyPair()
    }

    return Pair(
        RsaPrivateKey(kp.private, kp.public),
        RsaPublicKey(kp.public)
    )
}

// UnmarshalRsaPrivateKey returns a private key from the input x509 bytes
fun unmarshalRsaPrivateKey(data: ByteArray): PrivKey {
    TODO()
}

// UnmarshalRsaPublicKey returns a public key from the input x509 bytes
fun unmarshalRsaPublicKey(data: ByteArray): PubKey {
    TODO()
}