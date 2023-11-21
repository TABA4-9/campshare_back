import { faGithub } from "@fortawesome/free-brands-svg-icons"
import { faGraduationCap } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function Footer() {
    return (
        <div className="mt-auto mt-10">
            <div className="flex items-center text-center justify-center padding p-10 bg-zinc-300" style={{minHeight:"160px", maxHeight:"160px"}}>
                <div>
                    <div>
                        <div className="text-black text-lg font-normal font-['Asar']">CAMPSHARE </div>
                        <div className="pt-3">
                            <a href ="https://github.com/Sangeok"><FontAwesomeIcon icon={faGithub} /></a>
                            <a href ="https://www.dankook.ac.kr/web/kor"><FontAwesomeIcon className="pl-5" icon={faGraduationCap} /></a>
                        </div>
                    </div>
                    <div className="pt-3 text-stone-900 text-opacity-50 text-base font-medium font-['Poppins']">2023 all Right Reserved Term of use TABA_9</div>
                                    
                </div>
            </div>
        </div>
    )
}